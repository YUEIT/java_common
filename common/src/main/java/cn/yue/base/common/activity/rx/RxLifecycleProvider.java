package cn.yue.base.common.activity.rx;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class RxLifecycleProvider implements ILifecycleProvider<Event>, LifecycleObserver {

    private final BehaviorSubject<Event> lifecycleSubject = BehaviorSubject.create();

    @NonNull
    @CheckResult
    public final Observable<Event> lifecycle() {
        return this.lifecycleSubject.hide();
    }

    @NonNull
    @CheckResult
    private <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Event event) {
        return RxLifecycle.bindUntilEvent(this.lifecycleSubject, event);
    }

    @NonNull
    @CheckResult
    private <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bind(this.lifecycleSubject);
    }

    @Override
    public <T> SingleTransformer<T, T> toBindLifecycle() {
        return new SingleTransformer<T, T>() {

            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.
                        compose(bindUntilEvent(Event.ON_DESTROY))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public <T> SingleTransformer<T, T> toBindLifecycle(Event event) {
        return new SingleTransformer<T, T>() {

            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.
                        compose(bindUntilEvent(event))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onAny(LifecycleOwner owner, Lifecycle.Event event) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_ANY);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_CREATE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_START);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_STOP);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_RESUME);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_PAUSE);
    }

}
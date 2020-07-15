package cn.yue.base.middle.mvvm;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import cn.yue.base.common.activity.rx.ILifecycleProvider;
import cn.yue.base.common.activity.rx.LifecycleTransformer;
import cn.yue.base.common.activity.rx.RxLifecycle;
import cn.yue.base.common.activity.rx.RxLifecycleAndroid;
import cn.yue.base.middle.mvp.IWaitView;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class BaseViewModel extends AndroidViewModel implements ILifecycleProvider<Event>, IWaitView {

    public LoaderLiveData loader = new LoaderLiveData();
    public MutableLiveData<String> showWait = new MutableLiveData<>("");

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    private final BehaviorSubject<Event> lifecycleSubject = BehaviorSubject.create();

    @NonNull
    @CheckResult
    public final Observable<Event> lifecycle() {
        return this.lifecycleSubject.hide();
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Event event) {
        return RxLifecycle.bindUntilEvent(this.lifecycleSubject, event);
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
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
    protected void onAny(LifecycleOwner owner, Lifecycle.Event event) {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_ANY);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected void onCreate() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_CREATE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_START);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_STOP);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_RESUME);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        this.lifecycleSubject.onNext(Lifecycle.Event.ON_PAUSE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {}

    @Override
    public void showWaitDialog(String title) {
        showWait.postValue(title);
    }

    @Override
    public void dismissWaitDialog() {
        showWait.postValue("");
    }
}

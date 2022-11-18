package cn.yue.test.component;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.mvvm.PullViewModel;
import cn.yue.base.middle.net.observer.BasePullObserver;
import io.reactivex.rxjava3.core.Single;

public class TestPullViewModel extends PullViewModel {

    public TestPullViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void loadData() {
        Single.just("ssss")
                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(this.toBindLifecycle())
                .subscribe(new BasePullObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        ToastUtils.showShort(s);
                    }
                });
    }
}

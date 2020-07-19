package cn.yue.base.test.component;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.concurrent.TimeUnit;

import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.middle.components.BasePullFragment;
import cn.yue.base.middle.net.observer.BasePullSingleObserver;
import cn.yue.base.test.R;
import io.reactivex.Single;

@Route(path = "/app/testPull")
public class TestPullFragment extends BasePullFragment {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test_pull;
    }

    @Override
    protected void loadData() {
        Single.just("ssss")
                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(this.getLifecycleProvider().toBindLifecycle())
                .subscribe(new BasePullSingleObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        ToastUtils.showShortToast(s);
                    }
                });
    }
}

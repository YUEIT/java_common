package cn.yue.test.component;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.middle.mvvm.PageViewModel;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import cn.yue.base.test.data.TestItemBean;
import io.reactivex.rxjava3.core.Single;


public class TestPageViewModel extends PageViewModel<TestItemBean> {

    public TestPageViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void doLoadData(String nt) {
        getRequestSingle(nt)
                .compose(new PageTransformer())
                .subscribe();
    }

    protected Single<BaseListBean<TestItemBean>> getRequestSingle(String nt) {
        BaseListBean listBean = new BaseListBean();
        listBean.setPageSize(20);
        listBean.setTotal(22);
        List<TestItemBean> list = new ArrayList<>();
        for (int i=0; i < 20; i++) {
            TestItemBean testItemBean = new TestItemBean();
            testItemBean.setIndex(i);
            testItemBean.setName("this is " + i);
            list.add(testItemBean);
        }
        listBean.setList(list);
        return Single.just(listBean);
    }
}

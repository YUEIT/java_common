package cn.yue.base.test.widget;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.middle.mvp.IListPresenter;
import cn.yue.base.middle.mvp.IListView;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import cn.yue.base.test.data.TestItemBean;
import io.reactivex.Single;

/**
 * Description :
 * Created by yue on 2022/3/8
 */

public class ChildPresenter extends IListPresenter<BaseListBean<TestItemBean>, TestItemBean> {

    public ChildPresenter(IListView<TestItemBean> iView) {
        super(iView);
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

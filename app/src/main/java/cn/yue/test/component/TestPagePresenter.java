package cn.yue.test.component;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.middle.mvp.IListPresenter;
import cn.yue.base.middle.mvp.IListView;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import cn.yue.base.test.data.TestItemBean;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;

/**
 * Description :
 * Created by yue on 2022/3/8
 */

public class TestPagePresenter extends IListPresenter<BaseListBean<TestItemBean>, TestItemBean> {

    public TestPagePresenter(IListView<TestItemBean> iView) {
        super(iView);
    }

    @Override
    protected void doLoadData(String nt) {
        Single.create(new SingleOnSubscribe<BaseListBean<TestItemBean>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<BaseListBean<TestItemBean>> emitter) throws Exception {
                BaseListBean listBean = new BaseListBean();
                List<TestItemBean> list = new ArrayList<>();
                for (int i=0; i < 20; i++) {
                    TestItemBean testItemBean = new TestItemBean();
                    testItemBean.setName("this is " + i);
                    list.add(testItemBean);
                }
                listBean.setList(list);
                emitter.onSuccess(listBean);
            }
        }).compose(new PageTransformer())
                .subscribe();
    }
}

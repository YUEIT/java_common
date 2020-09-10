package cn.yue.base.test.widget;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.widget.viewpager.HeaderScrollHelper;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.components.BasePageFragment;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import cn.yue.base.test.R;
import cn.yue.base.test.data.TestItemBean;
import io.reactivex.Single;

public class ChildFragment2 extends BasePageFragment<TestItemBean> implements HeaderScrollHelper.ScrollableContainer {


    @Override
    protected boolean canPullDown() {
        return false;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_test;
    }

    @Override
    protected void bindItemData(CommonViewHolder<TestItemBean> holder, int position, TestItemBean testItemBean) {
        holder.setText(R.id.testTV, testItemBean.getName());
    }

    @Override
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

    @Override
    public View getScrollableView() {
        return findViewById(R.id.baseRV);
    }
}

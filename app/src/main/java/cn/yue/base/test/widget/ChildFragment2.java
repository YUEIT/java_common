package cn.yue.base.test.widget;

import android.view.View;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.common.widget.viewpager.HeaderScrollHelper;
import cn.yue.base.middle.mvp.components.BaseListFragment;
import cn.yue.base.test.R;
import cn.yue.base.test.data.TestItemBean;

public class ChildFragment2 extends BaseListFragment<TestItemBean> implements HeaderScrollHelper.ScrollableContainer {


    @Override
    protected boolean canPullDown() {
        return false;
    }

    @Override
    protected CommonAdapter<TestItemBean> initAdapter() {
        return new CommonAdapter<TestItemBean>(mActivity) {
            @Override
            public int getLayoutIdByType(int viewType) {
                return R.layout.item_test;
            }

            @Override
            public void bindData(CommonViewHolder<TestItemBean> holder, int position, TestItemBean testItemBean) {
                holder.setText(R.id.testTV, testItemBean.getName());
            }
        };
    }

    protected ChildPresenter childPresenter = new ChildPresenter(this);

    @Override
    protected void loadData(boolean isRefresh) {
        childPresenter.loadData(isRefresh);
    }

    @Override
    public View getScrollableView() {
        return findViewById(R.id.baseRV);
    }
}

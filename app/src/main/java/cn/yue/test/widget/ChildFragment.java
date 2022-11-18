package cn.yue.test.widget;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.mvp.components.BaseListFragment;
import cn.yue.base.test.data.TestItemBean;
import cn.yue.test.R;

public class ChildFragment extends BaseListFragment<TestItemBean> {

    protected ChildPresenter childPresenter = new ChildPresenter(this);

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

    @Override
    protected void loadData(boolean isRefresh) {
        childPresenter.loadData(isRefresh);
    }

}

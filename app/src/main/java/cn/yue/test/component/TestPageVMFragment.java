package cn.yue.test.component;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.common.widget.recyclerview.DiffRefreshAdapter;
import cn.yue.base.middle.mvvm.components.BasePageVMFragment;
import cn.yue.base.test.data.TestItemBean;
import cn.yue.test.R;

@Route(path = "/app/testPageVM")
public class TestPageVMFragment extends BasePageVMFragment<TestPageViewModel, TestItemBean> {

    @Override
    public CommonAdapter<TestItemBean> initAdapter() {
        return new DiffRefreshAdapter<TestItemBean>(mActivity) {
            @Override
            protected boolean areItemsTheSame(TestItemBean item1, TestItemBean item2) {
                return item1 == item2;
            }

            @Override
            protected boolean areContentsTheSame(TestItemBean oldItem, TestItemBean newItem) {
                return oldItem == newItem;
            }

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
}

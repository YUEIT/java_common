package cn.yue.base.test;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.mvvm.components.BasePageVMFragment;

@Route(path = "/app/testPullPageVM")
public class TestPageVMFragment extends BasePageVMFragment<TestItemBean, TestPageViewModel> {

    @Override
    public CommonAdapter<TestItemBean> getAdapter() {
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
}

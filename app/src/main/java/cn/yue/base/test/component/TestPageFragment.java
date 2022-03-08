package cn.yue.base.test.component;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.mvp.components.BaseListFragment;
import cn.yue.base.test.R;
import cn.yue.base.test.data.TestItemBean;

/**
 * Description :
 * Created by yue on 2019/6/11
 */

@Route(path = "/app/testPullList")
public class TestPageFragment extends BaseListFragment<TestItemBean> {

    @Override
    protected int getLayoutId() {
        return R.layout.test_fragment_base_pull_page;
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

    private final TestPagePresenter testPagePresenter = new TestPagePresenter(this);

    @Override
    protected void loadData(boolean isRefresh) {
        testPagePresenter.loadData(isRefresh);
    }

}

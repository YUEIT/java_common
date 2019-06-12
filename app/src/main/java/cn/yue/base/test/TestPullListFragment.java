package cn.yue.base.test;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.components.BasePullListFragment;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import io.reactivex.Single;

/**
 * Description :
 * Created by yue on 2019/6/11
 */

@Route(path = "/app/testPullList")
public class TestPullListFragment extends BasePullListFragment<BaseListBean<TestItemBean>, TestItemBean> {


    @Override
    protected int getLayoutId() {
        return R.layout.test_fragment_base_pull_page;
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
        List<TestItemBean> list = new ArrayList<>();
        for (int i=0; i < 20; i++) {
            TestItemBean testItemBean = new TestItemBean();
            testItemBean.setName("this is " + i);
            list.add(testItemBean);
        }
        listBean.setList(list);
        return Single.just(listBean);
    }
}

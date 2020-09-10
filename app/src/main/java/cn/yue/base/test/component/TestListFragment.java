package cn.yue.base.test.component;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.components.BaseListFragment;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import cn.yue.base.test.R;
import cn.yue.base.test.data.TestItemBean;
import io.reactivex.Single;

/**
 * Description :
 * Created by yue on 2019/6/11
 */

@Route(path = "/app/testPullList")
public class TestListFragment extends BaseListFragment<BaseListBean<TestItemBean>, TestItemBean> {

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTest();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_fragment_base_pull_page;
    }

    @Override
    protected int getItemType(int position) {
        if (position == 1) {
            return 1;
        }
        return super.getItemType(position);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == 1) {
            return R.layout.item_test_recyclerview;
        }
        return R.layout.item_test;
    }

    CommonAdapter<String> adapter;

    @Override
    protected void bindItemData(CommonViewHolder<TestItemBean> holder, int position, TestItemBean testItemBean) {
        if (getItemType(position) == 1) {
            RecyclerView recyclerView = holder.getView(R.id.rv);
            if (recyclerView.getLayoutManager() == null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(adapter = new CommonAdapter<String>(mActivity, testList){

                    @Override
                    public int getLayoutIdByType(int viewType) {
                        return R.layout.item_test;
                    }

                    @Override
                    public void bindData(CommonViewHolder<String> holder, int position, String s) {
                        holder.setText(R.id.testTV, s);
                    }
                });
            }
            adapter.notifyDataSetChanged();
        } else {
            holder.setText(R.id.testTV, testItemBean.getName());
            holder.setOnClickListener(R.id.testTV, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private List<String> testList = new ArrayList<>();
    private void initTest() {
        for (int i=0; i<10; i++) {
            testList.add("ssssa" + i);
        }
    }

    @Override
    protected Single<BaseListBean<TestItemBean>> getRequestSingle(String nt) {
        BaseListBean listBean = new BaseListBean();
        listBean.setPageSize(20);
        listBean.setTotal(22);
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

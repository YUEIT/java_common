package cn.yue.base.test.component;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
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
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Description :
 * Created by yue on 2019/6/11
 */

@Route(path = "/app/testPullList")
public class TestPageFragment extends BaseListFragment<BaseListBean<TestItemBean>, TestItemBean> {

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

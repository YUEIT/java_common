package cn.yue.base.test.component;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.components.BasePullFragment;
import cn.yue.base.middle.net.observer.BasePullObserver;
import cn.yue.base.test.R;
import io.reactivex.Single;

@Route(path = "/app/testPull")
public class TestPullFragment extends BasePullFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_pull_test;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test_pull;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.rv);
        List<String> testList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testList.add("ssssa" + i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        CommonAdapter<String> adapter = new CommonAdapter<String>(mActivity, testList) {

            @Override
            public int getLayoutIdByType(int viewType) {
                return R.layout.item_test;
            }

            @Override
            public void bindData(CommonViewHolder<String> holder, int position, String s) {
                holder.setText(R.id.testTV, s);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        Single.just("ssss")
                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(this.getLifecycleProvider().toBindLifecycle())
                .subscribe(new BasePullObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        ToastUtils.showShort(s);
                    }
                });
    }
}

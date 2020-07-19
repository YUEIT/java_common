package cn.yue.base.test.component;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.middle.mvvm.components.BasePullVMFragment;
import cn.yue.base.middle.router.RouterCard;
import cn.yue.base.test.R;

@Route(path = "/app/testPullVM")
public class TestPullVMFragment extends BasePullVMFragment<TestPullViewModel> {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test_pull_vm;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        findViewById(R.id.jump1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.navigation(new RouterCard().setPath("/app/testPullPageVM"));
            }
        });
        findViewById(R.id.jump2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.finish();
            }
        });
    }
}

package cn.yue.test.component;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.middle.mvvm.components.binding.BasePullVMBindFragment;
import cn.yue.test.BR;
import cn.yue.test.R;
import cn.yue.test.databinding.FragmentTestPullVmBinding;


@Route(path = "/app/testPullVM")
public class TestPullVMFragment extends BasePullVMBindFragment<TestPullViewModel, FragmentTestPullVmBinding> {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test_pull_vm;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        findViewById(R.id.jump2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.finish();
            }
        });
    }

    @Override
    public int getVariableId() {
        return BR.viewModel;
    }
}

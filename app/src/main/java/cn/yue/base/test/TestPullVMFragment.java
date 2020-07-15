package cn.yue.base.test;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.middle.mvvm.components.BasePullVMFragment;
import cn.yue.base.test.databinding.FragmentTestPullVmBinding;

@Route(path = "/app/testPullVM")
public class TestPullVMFragment extends BasePullVMFragment<FragmentTestPullVmBinding, TestPullViewModel> {

    @Override
    public int getVariableId() {
        return 0;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test_pull_vm;
    }

}

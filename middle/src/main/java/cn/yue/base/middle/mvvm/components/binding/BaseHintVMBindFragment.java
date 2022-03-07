package cn.yue.base.middle.mvvm.components.binding;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import cn.yue.base.middle.mvvm.BaseViewModel;
import cn.yue.base.middle.mvvm.components.BaseHintVMFragment;

public abstract class BaseHintVMBindFragment<VM extends BaseViewModel, T extends ViewDataBinding> extends BaseHintVMFragment<VM> {

    protected T binding;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        binding.setVariable(getVariableId(), viewModel);
    }

    public abstract int getVariableId();

    @Override
    protected void bindLayout(View inflated) {
        binding = DataBindingUtil.bind(inflated);
    }
}

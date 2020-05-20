package cn.yue.base.middle.mvvm.components;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.yue.base.middle.components.BaseHintFragment;
import cn.yue.base.middle.mvvm.BaseViewModel;

public abstract class BaseHintVMFragment<T extends ViewDataBinding, VM extends BaseViewModel> extends BaseHintFragment {

    protected T binding;
    protected VM viewModel;

    @Override
    protected void stubInflate(ViewStub stub, View inflated) {
        super.stubInflate(stub, inflated);
        binding = DataBindingUtil.bind(inflated);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        viewModel = getViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(modelClass);
        }
        binding.setVariable(getVariableId(), viewModel);
        getLifecycle().addObserver(viewModel);
    }

    public abstract int getVariableId();

    protected VM getViewModel() {
        return null;
    }

    public VM createViewModel(Class<VM> cls) {
        return new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(mActivity.getApplication())).get(cls);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(viewModel);
    }
}

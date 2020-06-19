package cn.yue.base.middle.mvvm.components;

import androidx.recyclerview.widget.RecyclerView;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.middle.R;
import cn.yue.base.middle.databinding.FragmentBasePullPageVmBinding;
import cn.yue.base.middle.mvvm.PullPageViewModel;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.refresh.IRefreshLayout;

public class BasePullPageVMFragment<S, VM extends PullPageViewModel<S>> extends BasePullListVMFragment<FragmentBasePullPageVmBinding, VM>{

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_pull_page_vm;
    }

    @Override
    protected PageHintView getPageHintView() {
        return binding.hintView;
    }

    @Override
    protected IRefreshLayout getRefreshLayout() {
        return binding.refreshL;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return binding.baseRV;
    }

    @Override
    public CommonAdapter getAdapter() {
        return null;
    }
}

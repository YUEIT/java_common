package cn.yue.base.middle.mvvm.components;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.middle.components.BasePullFooter;
import cn.yue.base.middle.mvp.IBaseView;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.mvp.PageStatus;
import cn.yue.base.middle.mvp.photo.IPhotoView;
import cn.yue.base.middle.mvp.photo.PhotoHelper;
import cn.yue.base.middle.mvvm.PullListViewModel;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.refresh.IRefreshLayout;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BasePullListVMFragment<T extends ViewDataBinding, VM extends PullListViewModel> extends BaseFragment implements IStatusView, IWaitView, IBaseView, IPhotoView {

    protected T binding;
    private CommonAdapter adapter;
    private BasePullFooter footer;
    private boolean isFirstLoading = true;
    private IRefreshLayout refreshL;
    private RecyclerView baseRV;
    protected PageHintView hintView;
    private PhotoHelper photoHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract PageHintView getPageHintView();
    protected abstract IRefreshLayout getRefreshLayout();
    protected abstract RecyclerView getRecyclerView();

    @Override
    protected void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.bind(cacheView);
        initViewMode();
        hintView = getPageHintView();
        hintView.setOnReloadListener(new PageHintView.OnReloadListener() {
            @Override
            public void onReload() {
                if (NetworkUtils.isConnected()) {
                    mActivity.recreateFragment(BasePullListVMFragment.this.getClass().getName());
                } else {
                    ToastUtils.showShortToast("网络不给力，请检查您的网络设置~");
                }
            }

            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    if (autoRefresh()) {
                        refreshWithLoading();
                    }
                } else {
                    showStatusView(PageStatus.STATUS_ERROR_NET);
                }
            }
        });

        refreshL = getRefreshLayout();
        refreshL.init();
        refreshL.setEnabled(canPullDown());
        refreshL.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });
        if (canPullDown()) {
            hintView.setRefreshTarget((ViewGroup) refreshL);
        }
        footer = getFooter();
        if (footer != null) {
            footer.setOnReloadListener(new BasePullFooter.OnReloadListener() {
                @Override
                public void onReload() {
                    viewModel.loadData();
                }
            });
        }
        baseRV = getRecyclerView();
        refreshL.setTargetView(baseRV);
        initRecyclerView(baseRV);
        baseRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                ArrayList dataList = (ArrayList)viewModel.dataList.getValue();
                if (dataList.isEmpty()) {
                    return;
                }
                boolean isTheLast = false;
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    isTheLast = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= dataList.size() - 1;
                } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    isTheLast = ((GridLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= dataList.size() - ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount() - 1;
                } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager)recyclerView.getLayoutManager();
                    int[] lastSpan = layoutManager.findLastVisibleItemPositions(null);
                    for (int position : lastSpan) {
                        if (position >= dataList.size() - layoutManager.getSpanCount() - 1) {
                            isTheLast = true;
                            break;
                        }
                    }
                }
                if (isTheLast && viewModel.status.getValue() == PageStatus.STATUS_SUCCESS) {
                    viewModel.status.postValue(PageStatus.STATUS_LOADING_ADD);
                    viewModel.footerStatus.postValue(PageStatus.STATUS_LOADING_ADD);
                    viewModel.loadData();
                }
            }
        });
    }

    protected VM viewModel;
    private void initViewMode() {
        viewModel = getViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = PullListViewModel.class;
            }
            viewModel = (VM) createViewModel(modelClass);
        }
        getLifecycle().addObserver(viewModel);
    }

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

    @Override
    protected void initOther() {
        super.initOther();
        if (NetworkUtils.isConnected()) {
            if (autoRefresh()) {
                refreshWithLoading();
            }
        } else {
            showStatusView(PageStatus.STATUS_ERROR_NET);
        }
        viewModel.dataList.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.status.observe(this, new Observer<PageStatus>() {
            @Override
            public void onChanged(PageStatus pageStatus) {
                showStatusView(pageStatus);
            }
        });
        viewModel.footerStatus.observe(this, new Observer<PageStatus>() {
            @Override
            public void onChanged(PageStatus pageStatus) {
                footer.showStatusView(pageStatus);
            }
        });
        viewModel.isRefresh.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean o) {
                if (o.booleanValue()) {
                    refreshL.startRefresh();
                } else {
                    refreshL.finishRefreshing();
                }
            }
        });
    }

    protected boolean autoRefresh() {
        return true;
    }

    protected boolean canPullDown() {
        return true;
    }

    protected void initRecyclerView(RecyclerView baseRV) {
        baseRV.setLayoutManager(getLayoutManager());
        baseRV.setAdapter(adapter = getAdapter());
        adapter.addFooterView(footer);
        viewModel.footerStatus.postValue(PageStatus.STATUS_NORMAL);
    }

    public abstract CommonAdapter getAdapter();

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    protected BasePullFooter getFooter() {
        if (footer == null) {
            footer = new BasePullFooter(mActivity);
        }
        return footer;
    }

    /**
     * 刷新 loading动画
     */
    public void refreshWithLoading() {
        baseRV.setVisibility(View.GONE);
        viewModel.refreshWithLoading();
    }

    @Override
    public void showStatusView(PageStatus status) {
        switch (status) {
            case STATUS_LOADING_REFRESH:
                showPageHintLoading();
                break;
            case STATUS_SUCCESS:
                showPageHintSuccess();
                break;
            case STATUS_END:
                showPageHintSuccess();
                footer.showStatusView(status);
                break;
            case STATUS_ERROR_NET:
                showPageHintErrorNet();
                break;
            case STATUS_ERROR_NO_DATA:
                showPageHintErrorNoData();
                break;
            case STATUS_ERROR_OPERATION:
                showPageHintErrorOperation();
                break;
            case STATUS_ERROR_SERVER:
                showPageHintErrorServer();
                break;
        }
    }

    private void showPageHintLoading() {
        if (hintView != null) {
            hintView.showLoading();
        }
    }

    private void showPageHintSuccess() {
        if (baseRV != null) {
            baseRV.setVisibility(View.VISIBLE);
        }
        if (hintView != null) {
            hintView.showSuccess();
        }
        isFirstLoading = false;
    }

    private void showPageHintErrorNet() {
        if (hintView != null) {
            if (isFirstLoading) {
                hintView.showErrorNet();
            } else {
                ToastUtils.showShortToast("网络不给力，请检查您的网络设置~");
            }
        }
    }

    private void showPageHintErrorNoData() {
        if (hintView != null) {
            hintView.showErrorNoData();
        }
    }

    private void showPageHintErrorOperation() {
        if (hintView != null && isFirstLoading) {
            hintView.showErrorOperation();
        }
    }

    private void showPageHintErrorServer() {
        if (hintView != null && isFirstLoading) {
            hintView.showErrorOperation();
        }
    }

    private WaitDialog waitDialog;
    @Override
    public void showWaitDialog(String title) {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(mActivity);
        }
        waitDialog.show(title, true, null);
    }

    @Override
    public void dismissWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.cancel();
        }
    }

    public PhotoHelper getPhotoHelper() {
        if (photoHelper == null) {
            photoHelper = new PhotoHelper(mActivity, this);
        }
        return photoHelper;
    }

    @Override
    public void selectImageResult(List<String> selectList) {

    }

    @Override
    public void cropImageResult(String image) {

    }

    @Override
    public void uploadImageResult(List<String> serverList) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (photoHelper != null) {
            photoHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}

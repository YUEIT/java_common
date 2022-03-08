package cn.yue.base.middle.mvvm.components;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.middle.R;
import cn.yue.base.middle.view.BaseFooter;
import cn.yue.base.middle.view.load.LoadStatus;
import cn.yue.base.middle.view.load.PageStatus;
import cn.yue.base.middle.mvvm.ListViewModel;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.PageStateView;
import cn.yue.base.middle.view.refresh.IRefreshLayout;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BaseListVMFragment<VM extends ListViewModel, S> extends BaseVMFragment<VM> {

    private CommonAdapter<S> adapter;
    private BaseFooter footer;
    private IRefreshLayout refreshL;
    protected PageStateView stateView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_pull_page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        stateView = findViewById(R.id.stateView);
        stateView.setOnReloadListener(new PageHintView.OnReloadListener() {
            @Override
            public void onReload() {
                if (NetworkUtils.isConnected()) {
                    if (autoRefresh()) {
                        viewModel.refresh();
                    }
                } else {
                    ToastUtils.showShort("网络不给力，请检查您的网络设置~");
                }
            }
        });
        refreshL = findViewById(R.id.refreshL);
        refreshL.setEnabledRefresh(canPullDown());
        refreshL.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });
        if (canPullDown()) {
            stateView.setRefreshTarget(refreshL);
        }
        footer = getFooter();
        if (footer != null) {
            footer.setOnReloadListener(new BaseFooter.OnReloadListener() {
                @Override
                public void onReload() {
                    viewModel.loadData();
                }
            });
        }
        RecyclerView baseRV = findViewById(R.id.baseRV);
        refreshL.setTargetView(baseRV);
        initRecyclerView(baseRV);
        addOnScrollListener(baseRV);
    }

    @Override
    protected void initOther() {
        super.initOther();
        if (NetworkUtils.isConnected()) {
            if (autoRefresh()) {
                viewModel.refresh();
            }
        } else {
            viewModel.loader.setPageStatus(PageStatus.NO_NET);
        }
        viewModel.dataLiveData.observe(this, new Observer<ArrayList<S>>() {
            @Override
            public void onChanged(ArrayList<S> list) {
                if (adapter != null) {
                    setData(list);
                }
            }
        });
        viewModel.loader.observePage(this, new Observer<PageStatus>() {
            @Override
            public void onChanged(PageStatus pageStatus) {
                showStatusView(pageStatus);
                refreshL.finishRefreshing();
            }
        });
        viewModel.loader.observeLoad(this, new Observer<LoadStatus>() {
            @Override
            public void onChanged(LoadStatus loadStatus) {
                if (loadStatus == LoadStatus.REFRESH) {
                    refreshL.startRefresh();
                } else {
                    refreshL.finishRefreshing();
                }
                footer.showStatusView(loadStatus);
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
        baseRV.setAdapter(adapter = initAdapter());
        adapter.addFooterView(footer);
    }

    private void addOnScrollListener(RecyclerView baseRV) {
        baseRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                viewModel.scrollToLoad(recyclerView.getLayoutManager());
            }
        });
    }

    public abstract CommonAdapter<S> initAdapter();

    public CommonAdapter<S> getAdapter() {
        return adapter;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    protected abstract void setData(List<S> list);

    protected BaseFooter getFooter() {
        if (footer == null) {
            footer = new BaseFooter(mActivity);
        }
        return footer;
    }

    private void showStatusView(PageStatus status) {
        if (stateView != null) {
            if (viewModel.loader.isFirstLoad()) {
                stateView.show(status);
            } else {
                stateView.show(PageStatus.NORMAL);
            }
        }
        if (status == PageStatus.NORMAL) {
            viewModel.loader.setFirstLoad(false);
        }
    }

}

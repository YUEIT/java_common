package cn.yue.base.middle.mvvm.components;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import androidx.lifecycle.Observer;

import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.R;
import cn.yue.base.middle.view.load.LoadStatus;
import cn.yue.base.middle.view.load.PageStatus;
import cn.yue.base.middle.mvvm.PullViewModel;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.PageStateView;
import cn.yue.base.middle.view.refresh.IRefreshLayout;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BasePullVMFragment<VM extends PullViewModel> extends BaseVMFragment<VM> {

    protected IRefreshLayout refreshL;
    protected PageStateView stateView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_pull;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        stateView = findViewById(R.id.stateView);
        stateView.setOnReloadListener(new PageHintView.OnReloadListener() {
            @Override
            public void onReload() {
                if (NetworkUtils.isConnected()) {
                    viewModel.refresh();
                } else {
                    ToastUtils.showShort("网络不给力，请检查您的网络设置~");
                }
            }
        });
        refreshL = findViewById(R.id.refreshL);
        refreshL.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });
        refreshL.setEnabledRefresh(canPullDown());
        if (canPullDown()) {
            stateView.setRefreshTarget(refreshL);
        }
        ViewStub baseVS = findViewById(R.id.baseVS);
        baseVS.setLayoutResource(getContentLayoutId());
        baseVS.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                bindLayout(inflated);
            }
        });
        baseVS.inflate();
    }

    protected void bindLayout(View inflated) { }

    @Override
    protected void initOther() {
        super.initOther();
        if (NetworkUtils.isConnected()) {
            viewModel.refresh();
        } else {
            viewModel.loader.setPageStatus(PageStatus.NO_NET);
        }
        viewModel.loader.observePage(this, new Observer<PageStatus>() {
            @Override
            public void onChanged(PageStatus pageStatus) {
                showStatusView(pageStatus);
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
            }
        });
    }

    protected abstract int getContentLayoutId();

    protected boolean canPullDown() {
        return true;
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

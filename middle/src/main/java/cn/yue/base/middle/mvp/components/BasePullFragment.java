package cn.yue.base.middle.mvp.components;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import java.util.List;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.middle.R;
import cn.yue.base.middle.mvp.IBaseView;
import cn.yue.base.middle.mvp.components.data.Loader;
import cn.yue.base.middle.mvp.photo.IPhotoView;
import cn.yue.base.middle.mvp.photo.PhotoHelper;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.PageStateView;
import cn.yue.base.middle.view.load.LoadStatus;
import cn.yue.base.middle.view.load.PageStatus;
import cn.yue.base.middle.view.refresh.IRefreshLayout;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BasePullFragment extends BaseFragment implements IBaseView, IPhotoView {

    private final Loader loader = new Loader();
    private IRefreshLayout refreshL;
    private PageStateView stateView;
    private PhotoHelper photoHelper;

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
                    refresh();
                } else {
                    ToastUtils.showShort(R.string.app_no_net);
                }
            }
        });
        refreshL = findViewById(R.id.refreshL);
        refreshL.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
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
        if (NetworkUtils.isConnected()) {
            refresh();
        } else {
            changePageStatus(PageStatus.NO_NET);
        }
    }

    protected abstract int getContentLayoutId();

    //回调继承 BasePullSingleObserver 以适应加载逻辑
    protected abstract void loadData();

    protected boolean canPullDown() {
        return true;
    }

    /**
     * 刷新 swipe动画
     */
    public void refresh() {
        refresh(loader.isFirstLoad());
    }

    /**
     * 刷新 选择是否页面加载动画
     */
    public void refresh(boolean isPageRefreshAnim) {
        if (loader.getPageStatus() == PageStatus.LOADING
                || loader.getLoadStatus() == LoadStatus.REFRESH) {
            return;
        }
        if (isPageRefreshAnim) {
            changePageStatus(PageStatus.LOADING);
        } else {
            changeLoadStatus(LoadStatus.REFRESH);
        }
        loadData();
    }

    private void showStatusView(PageStatus status) {
        if (stateView != null) {
            if (loader.isFirstLoad()) {
                stateView.show(status);
            } else {
                stateView.show(PageStatus.NORMAL);
            }
        }
        if (status == PageStatus.NORMAL) {
            loader.setFirstLoad(false);
        }
    }

    @Override
    public void changePageStatus(PageStatus status) {
        showStatusView(loader.setPageStatus(status));
        refreshL.finishRefreshing();
    }

    @Override
    public void changeLoadStatus(LoadStatus status) {
        loader.setLoadStatus(status);
        if (status == LoadStatus.REFRESH) {
            refreshL.startRefresh();
        } else {
            refreshL.finishRefreshing();
        }
    }

    private WaitDialog waitDialog;

    @Override
    public void showWaitDialog(String title) {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(mActivity);
        }
        waitDialog.show(title);
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

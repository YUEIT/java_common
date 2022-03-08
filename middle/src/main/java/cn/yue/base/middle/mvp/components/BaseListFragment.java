package cn.yue.base.middle.mvp.components;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.middle.R;
import cn.yue.base.middle.mvp.IListView;
import cn.yue.base.middle.mvp.components.data.Loader;
import cn.yue.base.middle.mvp.photo.IPhotoView;
import cn.yue.base.middle.mvp.photo.PhotoHelper;
import cn.yue.base.middle.view.BaseFooter;
import cn.yue.base.middle.view.PageHintView;
import cn.yue.base.middle.view.PageStateView;
import cn.yue.base.middle.view.load.LoadStatus;
import cn.yue.base.middle.view.load.PageStatus;
import cn.yue.base.middle.view.refresh.IRefreshLayout;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BaseListFragment<S> extends BaseFragment implements IListView<S>, IPhotoView {

    private CommonAdapter<S> adapter;
    private BaseFooter footer;
    private final Loader loader = new Loader();
    private IRefreshLayout refreshL;
    protected PageStateView stateView;
    private PhotoHelper photoHelper;

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
                        refresh();
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
                refresh();
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
                    loadData();
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
                refresh();
            }
        } else {
            changePageStatus(PageStatus.NO_NET);
        }
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
        changeLoadStatus(LoadStatus.NORMAL);
    }

    private void addOnScrollListener(RecyclerView baseRV) {
        baseRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                List<S> dataList = adapter.getList();
                if (adapter.getList().isEmpty()) {
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
                if (isTheLast
                        && loader.getPageStatus() == PageStatus.NORMAL
                        && loader.getLoadStatus() == LoadStatus.NORMAL) {
                    changeLoadStatus(LoadStatus.LOADING);
                    loadData();
                }
            }
        });
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    protected abstract CommonAdapter<S> initAdapter();

    protected CommonAdapter<S> getAdapter() {
        return adapter;
    }

    protected BaseFooter getFooter() {
        if (footer == null) {
            footer = new BaseFooter(mActivity);
        }
        return footer;
    }

    @Override
    public Loader getLoader() {
        return loader;
    }

    @Override
    public void setData(List<S> list) {
        adapter.setList(list);
    }

    /**
     * 刷新
     */
    public void refresh() {
        refresh(loader.isFirstLoad());
    }

    /**
     * 刷新
     */
    private void refresh(boolean isPageRefreshAnim) {
        if (loader.getLoadStatus() == LoadStatus.LOADING
                || loader.getLoadStatus() == LoadStatus.REFRESH
                || loader.getPageStatus() == PageStatus.LOADING) {
            return;
        }
        if (isPageRefreshAnim) {
            loader.setPageStatus(PageStatus.LOADING);
        } else {
            loader.setLoadStatus(LoadStatus.REFRESH);
        }
        loadData(true);
    }

    private void loadData() {
        loadData(false);
    }

    protected abstract void loadData(boolean isRefresh);

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
            footer.showStatusView(status);
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

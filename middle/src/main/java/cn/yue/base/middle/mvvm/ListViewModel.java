package cn.yue.base.middle.mvvm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.middle.components.load.LoadStatus;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.observer.BaseNetSingleObserver;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import io.reactivex.Single;

public abstract class ListViewModel<P extends BaseListBean<S>, S> extends BaseViewModel{

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    private String pageNt = "1";
    private String lastNt = "1";
    public int total = 0;    //当接口返回总数时，为返回数量；接口未返回数量，为统计数量；
    
    public MutableListLiveData<S> dataList = new MutableListLiveData<S>();

    protected String initPageNt() {
        return "1";
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
    public void refresh(boolean isPageRefreshAnim) {
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
        pageNt = initPageNt();
        loadData();
    }


    protected abstract Single<P> getRequestSingle(String nt);

    public void loadData() {
        if (getRequestSingle(pageNt) == null) {
            return;
        }
        getRequestSingle(pageNt)
                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(this.<P>toBindLifecycle())
                .subscribe(new BaseNetSingleObserver<P>() {

                    private boolean isLoadingRefresh = false;
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (loader.getPageStatus() == PageStatus.LOADING 
                                || loader.getLoadStatus() == LoadStatus.REFRESH) {
                            isLoadingRefresh = true;
                        } else {
                            isLoadingRefresh = false;
                        }
                    }

                    @Override
                    public void onSuccess(P p) {
                        if (isLoadingRefresh) {
                            dataList.setValue(new ArrayList<>());
                        }
                        if (isLoadingRefresh && p.getCurrentPageTotal() == 0) {
                            loadEmpty();
                        } else {
                            loadSuccess(p);
                            if (p.getCurrentPageTotal() < p.getPageSize()) {
                                loadNoMore();
                            } else if (p.getTotal() > 0 && p.getTotal() <= dataList.getValue().size()) {
                                loadNoMore();
                            } else if (p.getCurrentPageTotal() == 0) {
                                loadNoMore();
                            } else if (TextUtils.isEmpty(p.getPageNt()) && !initPageNt().matches("\\d+")) {
                                loadNoMore();
                            }
                        }
                        if (isLoadingRefresh) {
                            onRefreshComplete(p, null);
                        }
                    }

                    @Override
                    public void onException(ResultException e) {
                        loadFailed(e);
                        if (isLoadingRefresh) {
                            onRefreshComplete(null, e);
                        }
                    }

                    @Override
                    protected void onCancel(ResultException e) {
                        super.onCancel(e);
                        loadFailed(e);
                    }
                });
    }

    protected void loadSuccess(P p) {
        loader.setPageStatus(PageStatus.NORMAL);
        loader.setLoadStatus(LoadStatus.NORMAL);
        if (TextUtils.isEmpty(p.getPageNt())) {
            try {
                if (p.getPageNo() == 0) {
                    pageNt = String.valueOf(Integer.valueOf(pageNt + 1));
                } else {
                    pageNt = String.valueOf(p.getPageNo() + 1);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            pageNt = p.getPageNt();
        }
        if (p.getTotal() > 0) {
            total = p.getTotal();
        } else {
            total += p.getCurrentPageTotal();
        }
        lastNt = pageNt;
        dataList.addAll(p.getList() == null? new ArrayList<S>() : p.getList());
    }

    protected void loadFailed(ResultException e) {
        pageNt = lastNt;
        if (loader.isFirstLoad()) {
            if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                loader.setPageStatus(PageStatus.NO_NET);
            } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                loader.setPageStatus(PageStatus.NO_DATA);
            } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
                loader.setPageStatus(PageStatus.NO_NET);
                ToastUtils.showShortToast(e.getMessage());
            } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                loader.setPageStatus(PageStatus.ERROR);
                ToastUtils.showShortToast(e.getMessage());
            } else {
                loader.setPageStatus(PageStatus.ERROR);
                ToastUtils.showShortToast(e.getMessage());
            }
        } else {
            if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                loader.setLoadStatus(LoadStatus.NO_NET);
            } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                loader.setLoadStatus(LoadStatus.NO_DATA);
            } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
                loader.setLoadStatus(LoadStatus.NORMAL);
                ToastUtils.showShortToast(e.getMessage());
            } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                loader.setLoadStatus(LoadStatus.NORMAL);
                ToastUtils.showShortToast(e.getMessage());
            } else {
                loader.setLoadStatus(LoadStatus.NORMAL);
                ToastUtils.showShortToast(e.getMessage());
            }
        }
    }

    protected void loadNoMore() {
        loader.setLoadStatus(LoadStatus.END);
    }

    protected void loadEmpty() {
        total = 0;
        dataList.clear();
        if (showSuccessWithNoData()) {
            loader.setPageStatus(PageStatus.NORMAL);
            loader.setLoadStatus(LoadStatus.NO_DATA);
        } else {
            loader.setPageStatus(PageStatus.NO_DATA);
            loader.setLoadStatus(LoadStatus.NORMAL);
        }
    }

    protected boolean showSuccessWithNoData() {
        return false;
    }

    protected void onRefreshComplete(P p, ResultException e) {

    }

    public void hasLoad(RecyclerView.LayoutManager layoutManager) {
        if (dataList.size() <= 0) {
            return;
        }
        boolean isTheLast = false;
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager lm = (GridLayoutManager)layoutManager;
            isTheLast = lm.findLastVisibleItemPosition() >= dataList.size() - lm.getSpanCount() - 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager)layoutManager;
            int[] lastSpan = lm.findLastVisibleItemPositions(null);
            for (int position : lastSpan) {
                if (position >= dataList.size() - lm.getSpanCount() - 1) {
                    isTheLast = true;
                    break;
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            isTheLast = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition() >= dataList.size() - 1;
        }
        if (isTheLast && loader.getPageStatus() == PageStatus.NORMAL 
                && loader.getLoadStatus() == LoadStatus.NORMAL) {
            loader.setLoadStatus(LoadStatus.LOADING);
            loadData();
        }
    }
}

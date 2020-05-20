package cn.yue.base.middle.mvvm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.middle.mvp.PageStatus;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.observer.BaseNetSingleObserver;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import io.reactivex.Single;

public abstract class PullListViewModel<P extends BaseListBean<S>, S> extends BaseViewModel{

    public PullListViewModel(@NonNull Application application) {
        super(application);
    }

    private String pageNt = "1";
    private String lastNt = "1";
    public int total = 0;    //当接口返回总数时，为返回数量；接口未返回数量，为统计数量；
    public MutableListLiveData<S> dataList = new MutableListLiveData<>();
    public MutableLiveData<PageStatus> status = new MutableLiveData<>(PageStatus.STATUS_NORMAL);
    public MutableLiveData<PageStatus> footerStatus = new MutableLiveData<>(PageStatus.STATUS_NORMAL);
    public MutableLiveData<Boolean> isRefresh = new MutableLiveData<>();

    protected String initPageNt() {
        return "1";
    }

    /**
     * 刷新 loading动画
     */
    public void refreshWithLoading() {
        status.postValue(PageStatus.STATUS_LOADING_REFRESH);
        refresh(false);
    }

    /**
     * 刷新 swipe动画
     */
    public void refresh() {
        refresh(true);
    }

    /**
     * 刷新 选择是否有swipe动画
     * @param hasRefreshAnim
     */
    public void refresh(boolean hasRefreshAnim) {
        if (status.getValue() == PageStatus.STATUS_LOADING_ADD || status.getValue() == PageStatus.STATUS_LOADING_REFRESH) {
            return;
        }
        status.postValue(PageStatus.STATUS_LOADING_REFRESH);
        if (hasRefreshAnim) {
            isRefresh.postValue(true);
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
//                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(this.<P>toBindLifecycle())
                .subscribe(new BaseNetSingleObserver<P>() {

                    private boolean isLoadingRefresh = false;
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (status.getValue() == PageStatus.STATUS_LOADING_REFRESH) {
                            isLoadingRefresh = true;
                        } else {
                            isLoadingRefresh = false;
                        }
                    }

                    @Override
                    public void onSuccess(P p) {
                        isRefresh.postValue(false);
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
                        isRefresh.postValue(false);
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
        status.setValue(PageStatus.STATUS_SUCCESS);
        footerStatus.setValue(PageStatus.STATUS_SUCCESS);
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
        if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
            if (this.status.getValue() == PageStatus.STATUS_LOADING_REFRESH) {
                status.postValue(PageStatus.STATUS_ERROR_NET);
            } else if (this.status.getValue() == PageStatus.STATUS_LOADING_ADD) {
                footerStatus.postValue(PageStatus.STATUS_LOADING_ADD);
            }
        } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
            status.postValue(PageStatus.STATUS_ERROR_NO_DATA);
        } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
            status.postValue(PageStatus.STATUS_SUCCESS);
            footerStatus.postValue(PageStatus.STATUS_SUCCESS);
        } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
            status.postValue(PageStatus.STATUS_ERROR_OPERATION);
            ToastUtils.showShortToast(e.getMessage());
        } else {
            status.postValue(PageStatus.STATUS_ERROR_SERVER);
            ToastUtils.showShortToast(e.getMessage());
        }
    }

    protected void loadNoMore() {
        status.postValue(PageStatus.STATUS_END);
    }

    protected void loadEmpty() {
        total = 0;
        dataList.clear();
        if (showSuccessWithNoData()) {
            status.postValue(PageStatus.STATUS_SUCCESS);
            footerStatus.postValue(PageStatus.STATUS_ERROR_NO_DATA);
        } else {
            status.postValue(PageStatus.STATUS_ERROR_NO_DATA);
        }
    }

    protected boolean showSuccessWithNoData() {
        return false;
    }

    protected void onRefreshComplete(P p, ResultException e) {

    }

}

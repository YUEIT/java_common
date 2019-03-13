package cn.yue.base.middle.components;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.R;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.PageStatus;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.observer.BaseNetSingleObserver;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import cn.yue.base.middle.view.PageHintView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public abstract class BasePullListFragment<P extends BaseListBean<S>, S> extends BaseFragment implements IStatusView {

    private String pageNt = "0";
    private List<S> dataList = new ArrayList<>();
    protected int total;    //当接口返回总数时，为返回数量；接口未返回数量，为统计数量；
    private CommonAdapter<S> adapter;
    private BasePullFooter footer;
    private PageStatus status = PageStatus.STATUS_NORMAL;
    private SwipeRefreshLayout refreshL;
    protected PageHintView hintView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_pull_page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        refreshL = findViewById(R.id.refreshL);
        refreshL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        RecyclerView baseRV = findViewById(R.id.baseRV);
        baseRV.setLayoutManager(getLayoutManager());
        baseRV.setAdapter(adapter = getAdapter());
        baseRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean isTheLast = false;
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    isTheLast = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= dataList.size();
                } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    isTheLast = ((GridLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= dataList.size() - ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
                }
                if (isTheLast && (status == PageStatus.STATUS_NORMAL || status == PageStatus.STATUS_SUCCESS)) {
                    status = PageStatus.STATUS_LOADING_ADD;
                    adapter.addFooterView(footer);
                    footer.showStatusView(status);
                    loadData();
                }
            }
        });
        footer = getFooter();
        if (footer != null) {
            footer.setOnReloadListener(new BasePullFooter.OnReloadListener() {
                @Override
                public void onReload() {
                    loadData();
                }
            });
        }
        hintView = findViewById(R.id.hintView);
        refresh();
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    protected CommonAdapter<S> getAdapter() {
        return new CommonAdapter<S>(mActivity, new ArrayList<S>()) {
            @Override
            public int getLayoutIdByType(int viewType) {
                return getItemLayoutId();
            }

            @Override
            public void bindData(CommonViewHolder<S> holder, int position, S s) {
                bindItemData(holder, position, s);
            }
        };
    }

    protected abstract int getItemLayoutId();

    protected abstract void bindItemData(CommonViewHolder<S> holder, int position, S s);

    protected BasePullFooter getFooter() {
        return new BasePullFooter(mActivity);
    }

    public void refresh() {
        if (status == PageStatus.STATUS_LOADING_ADD || status == PageStatus.STATUS_LOADING_REFRESH) {
            return;
        }
        status = PageStatus.STATUS_LOADING_REFRESH;
        refreshL.setRefreshing(true);
        pageNt = initPageNt();
        loadData();
    }

    protected String initPageNt() {
        return "0";
    }

    protected abstract Single<P> getRequestSingle(String nt);

    private void loadData() {
        if (getRequestSingle(pageNt) == null) {
            return;
        }
        getRequestSingle(pageNt)
                .compose(this.<P>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSingleObserver<P>() {
                    @Override
                    public void onSuccess(P p) {
                        if (refreshL.isRefreshing()) {
                            refreshL.setRefreshing(false);
                        }
                        if (status == PageStatus.STATUS_LOADING_REFRESH) {
                            dataList.clear();
                        }
                        if (status == PageStatus.STATUS_LOADING_REFRESH && p.getCurrentPageTotal() == 0) {
                            loadEmpty();
                        } else {
                            loadSuccess(p);
                            if (p.getCurrentPageTotal() < p.getPageSize()) {
                                loadNoMore();
                            }
                        }
                    }

                    @Override
                    public void onException(ResultException e) {
                        if (refreshL.isRefreshing()) {
                            refreshL.setRefreshing(false);
                        }
                        loadFailed(e);
                    }
                });
    }

    protected void loadSuccess(P p) {
        showStatusView(PageStatus.STATUS_SUCCESS);
        if (TextUtils.isEmpty(p.getPageNt())) {
            if (p.getPageNo() == 0) {
                pageNt = String.valueOf(Integer.valueOf(pageNt) + 1);
            } else {
                pageNt = String.valueOf(p.getPageNo() + 1);
            }
        } else {
            pageNt = p.getPageNt();
        }
        if (p.getTotal() > 0) {
            total = p.getTotal();
        } else {
            total += p.getCurrentPageTotal();
        }
        dataList = p.getList() == null? new ArrayList<S>() : p.getList();
        adapter.setList(dataList);
    }

    protected void loadFailed(ResultException e) {
        if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
            showStatusView(PageStatus.STATUS_ERROR_NET);
        } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
            showStatusView(PageStatus.STATUS_ERROR_NO_DATA);
        } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
            this.status = PageStatus.STATUS_ERROR_OPERATION;
            showHintErrorOperation(e.getMessage());
        } else {
            this.status = PageStatus.STATUS_ERROR_SERVER;
            showHintErrorServer(e.getMessage());
        }
    }

    protected void loadNoMore() {
        if (adapter.getFooterView() == null || adapter.getFooterView() != footer) {
            adapter.addFooterView(footer);
        }
        showStatusView(PageStatus.STATUS_END);
    }

    protected void loadEmpty() {
        total = 0;
        dataList.clear();
        adapter.setList(dataList);
        showStatusView(PageStatus.STATUS_ERROR_NO_DATA);
    }

    @Override
    public void showStatusView(PageStatus status) {
        switch (status) {
            case STATUS_SUCCESS:
                showPageHintSuccess();
                adapter.removeFooterView(footer);
                break;
            case STATUS_END:
                showPageHintSuccess();
                footer.showStatusView(status);
                break;
            case STATUS_ERROR_NET:
                if (this.status == PageStatus.STATUS_LOADING_REFRESH) {
                    showPageHintErrorNet();
                } else if (this.status == PageStatus.STATUS_LOADING_ADD) {
                    footer.showStatusView(status);
                }
                break;
            case STATUS_ERROR_NO_DATA:
                showPageHintErrorNoData();
                adapter.removeFooterView(footer);
                break;
        }
        this.status = status;
    }


    protected void showPageHintSuccess() {
        if (hintView != null) {
            hintView.setVisibility(View.GONE);
        }
    }

    protected void showPageHintErrorNet() {
        if (hintView != null) {
            hintView.setVisibility(View.VISIBLE);
            hintView.setHintText("网络连接异常~");
        }
    }

    protected void showPageHintErrorNoData() {
        if (hintView != null) {
            hintView.setVisibility(View.VISIBLE);
            hintView.setHintText("无数据~");
        }
    }

    protected void showHintErrorOperation(String errorMessage) {
        ToastUtils.showShortToast(errorMessage);
    }

    protected void showHintErrorServer(String errorMessage) {
        ToastUtils.showShortToast(errorMessage);
    }
}

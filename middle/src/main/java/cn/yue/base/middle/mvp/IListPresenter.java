package cn.yue.base.middle.mvp;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.view.load.LoadStatus;
import cn.yue.base.middle.mvp.components.data.Loader;
import cn.yue.base.middle.view.load.PageStatus;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.observer.BaseNetObserver;
import cn.yue.base.middle.net.wrapper.IListModel;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Description :
 * Created by yue on 2022/3/8
 */

public abstract class IListPresenter<P extends IListModel<S>, S> {

    public IListPresenter(IListView<S> iView) {
        this.iView = iView;
        this.loader = iView.getLoader();
    }

    private final IListView<S> iView;
    private final Loader loader;
    private String pageNt = "1";
    private String lastNt = "1";
    protected List<S> dataList = new ArrayList<>();
    //当接口返回总数时，为返回数量；接口未返回数量，为统计数量；
    protected int total;

    protected String initPageNt() {
        return "1";
    }

    protected int initPageSize() {
        return 20;
    }

    public void loadData(boolean isRefresh) {
        if (isRefresh) {
            pageNt = initPageNt();
        }
        doLoadData(pageNt);
    }

    protected abstract void doLoadData(String nt);

    public class PageTransformer implements SingleTransformer<P, P> {

        BaseNetObserver<P> pageObserver;

        public PageTransformer() {
            this(new PageObserver());
        }

        public PageTransformer(BaseNetObserver<P> pageObserver) {
            this.pageObserver = pageObserver;
        }

        @NonNull
        @Override
        public SingleSource<P> apply(@NonNull Single<P> upstream) {
            return upstream
                    .compose(iView.getLifecycleProvider().toBindLifecycle())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            pageObserver.onStart();
                        }
                    })
                    .doOnSuccess(new Consumer<P>() {
                        @Override
                        public void accept(P p) throws Exception {
                            pageObserver.onSuccess(p);
                        }
                    })
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            pageObserver.onError(throwable);
                        }
                    });
        }
    }

    public class PageObserver extends BaseNetObserver<P> {
        private boolean isLoadingRefresh = false;
        @Override
        public void onStart() {
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
                dataList.clear();
            }
            if (isLoadingRefresh && p.getCurrentPageTotal() == 0) {
                loadEmpty();
            } else {
                loadSuccess(p);
                if (p.getCurrentPageTotal() < p.getPageSize()
                        || (p.getTotal() > 0 && p.getTotal() <= dataList.size())
                        || p.getCurrentPageTotal() == 0
                        || (TextUtils.isEmpty(p.getPageNt()) && !initPageNt().matches("\\d+"))
                        || p.getCurrentPageTotal() < initPageSize()) {
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

        protected void loadSuccess(P p) {
            iView.changePageStatus(PageStatus.NORMAL);
            iView.changeLoadStatus(LoadStatus.NORMAL);
            if (TextUtils.isEmpty(p.getPageNt())) {
                try {
                    if (p.getPageNo() == 0) {
                        pageNt = String.valueOf(Integer.valueOf(pageNt) + 1);
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
            iView.setData(dataList);
        }

        protected void loadFailed(ResultException e) {
            pageNt = lastNt;
            if (loader.isFirstLoad()) {
                if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                    iView.changePageStatus(PageStatus.NO_NET);
                } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                    iView.changePageStatus(PageStatus.NO_DATA);
                } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
                    iView.changePageStatus(PageStatus.ERROR);
                } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                    iView.changePageStatus(PageStatus.ERROR);
                    ToastUtils.showShort(e.getMessage());
                } else {
                    iView.changePageStatus(PageStatus.ERROR);
                    ToastUtils.showShort(e.getMessage());
                }
            } else {
                if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                    iView.changeLoadStatus(LoadStatus.NO_NET);
                } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                    iView.changeLoadStatus(LoadStatus.NORMAL);
                } else if (NetworkConfig.ERROR_CANCEL.equals(e.getCode())) {
                    iView.changeLoadStatus(LoadStatus.NORMAL);
                } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                    iView.changeLoadStatus(LoadStatus.NORMAL);
                    ToastUtils.showShort(e.getMessage());
                } else {
                    iView.changeLoadStatus(LoadStatus.NORMAL);
                    ToastUtils.showShort(e.getMessage());
                }
            }
        }

        protected void loadNoMore() {
            iView.changeLoadStatus(LoadStatus.END);
        }

        protected void loadEmpty() {
            total = 0;
            dataList.clear();
            iView.setData(dataList);
            if (showSuccessWithNoData()) {
                iView.changePageStatus(PageStatus.NORMAL);
                iView.changeLoadStatus(LoadStatus.NO_DATA);
            } else {
                iView.changePageStatus(PageStatus.NO_DATA);
            }
        }

        protected void onRefreshComplete(P p, ResultException e) { }
    }

    protected boolean showSuccessWithNoData() {
        return false;
    }

    public class PageDelegateObserver extends BaseNetObserver<P> {

        private final BaseNetObserver<P> observer;
        private final BaseNetObserver<P> pageObserver;

        PageDelegateObserver(BaseNetObserver<P> observer) {
            this(observer, new PageObserver());
        }

        PageDelegateObserver(BaseNetObserver<P> observer, BaseNetObserver<P> pageObserver) {
            this.observer = observer;
            this.pageObserver = pageObserver;
        }

        @Override
        public void onStart() {
            super.onStart();
            pageObserver.onStart();
            if (observer != null) {
                observer.onStart();
            }
        }

        @Override
        public void onSuccess(@NonNull P p) {
            pageObserver.onSuccess(p);
            if (observer != null) {
                observer.onSuccess(p);
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            pageObserver.onError(e);
            if (observer != null) {
                observer.onError(e);
            }
        }

        @Override
        protected void onCancel(ResultException e) {
            super.onCancel(e);
        }

        @Override
        public void onException(ResultException e) { }
    }
}

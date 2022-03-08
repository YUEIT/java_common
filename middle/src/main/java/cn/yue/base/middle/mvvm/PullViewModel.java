package cn.yue.base.middle.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;

import cn.yue.base.middle.view.load.LoadStatus;
import cn.yue.base.middle.view.load.PageStatus;
import cn.yue.base.middle.mvp.IStatusView;

public abstract class PullViewModel extends BaseViewModel implements IStatusView {

    public PullViewModel(@NonNull Application application) {
        super(application);
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
        if (loader.getLoadStatus() == LoadStatus.REFRESH
                || loader.getPageStatus() == PageStatus.LOADING) {
            return;
        }
        if (isPageRefreshAnim) {
            loader.setPageStatus(PageStatus.LOADING);
        } else {
            loader.setLoadStatus(LoadStatus.REFRESH);
        }
        loadData();
    }

    protected abstract void loadData();

    @Override
    public void changePageStatus(PageStatus status) {
        loader.setPageStatus(status);
    }

    @Override
    public void changeLoadStatus(LoadStatus status) {
        loader.setLoadStatus(status);
    }
}

package cn.yue.base.middle.mvp.components.data;

import cn.yue.base.middle.view.load.LoadStatus;
import cn.yue.base.middle.view.load.PageStatus;

public class Loader {
    private boolean isFirstLoad;
    private PageStatus pageStatus;
    private LoadStatus loadStatus;

    public Loader() {
        pageStatus = PageStatus.NORMAL;
        loadStatus = LoadStatus.NORMAL;
        isFirstLoad = true;
    }

    public void setFirstLoad(boolean firstLoad) {
        isFirstLoad = firstLoad;
    }

    public LoadStatus setLoadStatus(LoadStatus loadStatus) {
        this.loadStatus = loadStatus;
        return loadStatus;
    }

    public PageStatus setPageStatus(PageStatus pageStatus) {
        this.pageStatus = pageStatus;
        return pageStatus;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    public PageStatus getPageStatus() {
        return pageStatus;
    }

    public LoadStatus getLoadStatus() {
        return loadStatus;
    }
}

package cn.yue.base.middle.net.observer;


import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.view.load.LoadStatus;
import cn.yue.base.middle.view.load.PageStatus;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;

/**
 * Description :
 * Created by yue on 2019/4/1
 */

public abstract class BasePullObserver<T> extends BaseNetObserver<T> {

    private IStatusView iStatusView;

    public BasePullObserver(IStatusView iStatusView) {
        this.iStatusView = iStatusView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(T t) {
        if (iStatusView != null) {
            iStatusView.changeLoadStatus(LoadStatus.NORMAL);
            iStatusView.changePageStatus(PageStatus.NORMAL);
        }
        onNext(t);
    }

    @Override
    public void onException(ResultException e) {
        if (iStatusView != null) {
            if (NetworkConfig.ERROR_NO_NET.equals(e.getCode())) {
                iStatusView.changePageStatus(PageStatus.NO_NET);
            } else if (NetworkConfig.ERROR_NO_DATA.equals(e.getCode())) {
                iStatusView.changePageStatus(PageStatus.NO_DATA);
            } else if (NetworkConfig.ERROR_OPERATION.equals(e.getCode())) {
                iStatusView.changePageStatus(PageStatus.ERROR);
                ToastUtils.showShort(e.getMessage());
            } else {
                iStatusView.changePageStatus(PageStatus.ERROR);
                ToastUtils.showShort(e.getMessage());
            }
        }
    }

    @Override
    protected void onCancel(ResultException e) {
        super.onCancel(e);
        if (iStatusView != null) {
            iStatusView.changeLoadStatus(LoadStatus.NORMAL);
        }
    }

    public abstract void onNext(T t);
}

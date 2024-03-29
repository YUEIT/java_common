package cn.yue.base.middle.net.observer;

import java.util.concurrent.CancellationException;

import cn.yue.base.common.utils.variable.ResourceUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.middle.R;
import cn.yue.base.middle.module.IAppModule;
import cn.yue.base.middle.module.manager.ModuleManager;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;

/**
 * Description :
 * Created by yue on 2019/3/6
 */

public abstract class BaseNetObserver<T> extends DisposableSingleObserver<T> {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onError(Throwable e) {
        ResultException resultException;
        if (e instanceof ResultException) {
            resultException = (ResultException) e;
            if (NetworkConfig.ERROR_TOKEN_INVALID.equals(resultException.getCode())
                    || NetworkConfig.ERROR_LOGIN_INVALID.equals(resultException.getCode())) {
                onLoginInvalid();
                return;
            }
            onException(resultException);
        } else if (e instanceof CancellationException) {
            onCancel(new ResultException(NetworkConfig.ERROR_CANCEL, ResourceUtils.getString(R.string.app_request_cancel)));
        } else {
            onException(new ResultException(NetworkConfig.ERROR_OPERATION, e.getMessage()));
        }
    }

    public abstract void onException(ResultException e);

    protected void  onCancel(ResultException e) {}

    private void onLoginInvalid() {
        IAppModule iAppModule = ModuleManager.getModuleService(IAppModule.class);
        ToastUtils.showShort(R.string.app_login_fail);
        iAppModule.loginInvalid();
    }
}

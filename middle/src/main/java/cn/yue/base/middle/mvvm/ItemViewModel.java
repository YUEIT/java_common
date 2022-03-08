package cn.yue.base.middle.mvvm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import cn.yue.base.common.activity.rx.RxLifecycleTransformer;
import cn.yue.base.middle.mvvm.data.RouterModel;
import cn.yue.base.middle.router.RouterCard;
import io.reactivex.SingleTransformer;

public abstract class ItemViewModel extends BaseViewModel {

    private final BaseViewModel parentViewModel;

    public ItemViewModel(@NonNull BaseViewModel parentViewModel) {
        super(parentViewModel.getApplication());
        this.parentViewModel = parentViewModel;
    }

    @Override
    public <T> RxLifecycleTransformer<T> toBindLifecycle() {
        return parentViewModel.toBindLifecycle();
    }

    @Override
    public <T> RxLifecycleTransformer<T> toBindLifecycle(Lifecycle.Event event) {
        return parentViewModel.toBindLifecycle(event);
    }

    @Override
    public void showWaitDialog(String title) {
        parentViewModel.showWaitDialog(title);
    }

    @Override
    public void dismissWaitDialog() {
        parentViewModel.dismissWaitDialog();
    }

    public void navigation(RouterModel routerModel) {
        parentViewModel.navigation(routerModel);
    }

    @Override
    public void finish() {
        parentViewModel.finish();
    }

    @Override
    public void finishForResult(int resultCode, Bundle bundle) {
        parentViewModel.finishForResult(resultCode, bundle);
    }
}

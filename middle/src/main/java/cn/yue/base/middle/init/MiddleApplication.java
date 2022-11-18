package cn.yue.base.middle.init;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import cn.yue.base.common.activity.CommonApplication;
import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.middle.module.IBaseModule;
import cn.yue.base.middle.module.ModuleType;
import cn.yue.base.middle.module.manager.ModuleManager;
import cn.yue.base.middle.router.FRouter;

public abstract class MiddleApplication extends CommonApplication implements ViewModelStoreOwner {

    @Override
    protected void init() {
        FRouter.init(this);
        BaseUrlAddress.setDebug(InitConstant.isDebug());
        LogUtils.getConfig().setLogSwitch(InitConstant.isDebug());
        ModuleManager.register(ModuleType.MODULE_BASE, IBaseModule.class, new BaseModuleService());
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    private final ViewModelStore viewModelStore = new ViewModelStore();

    private ViewModelProvider getViewModelProvider() {
        return new ViewModelProvider((ViewModelStoreOwner) this,
                (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(this));
    }

    public <VM extends ViewModel> VM createViewModel(Class<VM> cls) {
        return getViewModelProvider().get(cls);
    }

}

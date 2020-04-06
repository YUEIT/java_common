package cn.yue.base.test;

import cn.yue.base.middle.init.MiddleApplication;
import cn.yue.base.middle.module.IAppModule;
import cn.yue.base.middle.module.ModuleType;
import cn.yue.base.middle.module.manager.ModuleManager;

/**
 * Description :
 * Created by yue on 2018/11/14
 */
public class AppApplication extends MiddleApplication {

    @Override
    public void registerModule() {
        ModuleManager.register(ModuleType.MODULE_APP, IAppModule.class, new AppModuleImpl());
    }
}

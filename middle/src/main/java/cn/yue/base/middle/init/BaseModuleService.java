package cn.yue.base.middle.init;

import android.app.Application;
import android.content.Context;

import cn.yue.base.middle.module.IBaseModule;
import cn.yue.base.middle.router.FRouter;

public class BaseModuleService implements IBaseModule {

    @Override
    public void init(Context context) {
        NotificationConfig.initChannel();
        AutoSizeInitUtils.init((Application) context);
    }
}

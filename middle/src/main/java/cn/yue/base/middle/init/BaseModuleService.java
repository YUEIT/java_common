package cn.yue.base.middle.init;

import android.content.Context;

import cn.yue.base.middle.module.IBaseModule;

public class BaseModuleService implements IBaseModule {

    @Override
    public void init(Context context) {
        BaseUrlAddress.setDebug(InitConstant.isDebug);
    }
}

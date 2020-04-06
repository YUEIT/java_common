package cn.yue.base.test;

import android.content.Context;
import android.util.Log;

import cn.yue.base.middle.module.IAppModule;

public class AppModuleImpl implements IAppModule {

    @Override
    public void toOrderPay() {
        Log.d("luobiao", "toOrderPay");
    }

    @Override
    public void init(Context context) {

    }
}

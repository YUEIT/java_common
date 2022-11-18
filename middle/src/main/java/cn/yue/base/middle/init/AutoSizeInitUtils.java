package cn.yue.base.middle.init;

import android.app.Activity;
import android.app.Application;

import cn.yue.base.common.utils.device.ScreenUtils;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.DefaultAutoAdaptStrategy;

/**
 * Description :
 * Created by yue on 2022/11/18
 */

public class AutoSizeInitUtils {

    public static void init(Application application) {
        AutoSize.checkAndInit(application);
        AutoSizeConfig.getInstance()
                .setAutoAdaptStrategy(new CustomAutoAdapterStrategy());
    }

    public static class CustomAutoAdapterStrategy extends DefaultAutoAdaptStrategy {
        @Override
        public void applyAdapt(Object target, Activity activity) {
            super.applyAdapt(target, activity);
            if (((float)ScreenUtils.getScreenWidth() / (float) ScreenUtils.getScreenHeight()) > 0.75f) {
                AutoSize.cancelAdapt(activity);
            }
        }
    }
}

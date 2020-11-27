package cn.yue.base.test.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import cn.yue.base.middle.mvvm.BaseViewModel;
import cn.yue.base.middle.router.FRouter;
import cn.yue.base.middle.router.RouterCard;

class JumpUtils {

    public static void toTest(Object target) {
        navigation(FRouter.getInstance().build("/app/test"), target);
    }

    public static void navigation(RouterCard routerCard, Object target) {
        navigation(routerCard, target, 0);
    }

    public static void navigation(RouterCard routerCard, Object target, int requestCode) {
        navigation(routerCard, target, requestCode, null);
    }

    public static void navigation(RouterCard routerCard, Object target, int requestCode, String toActivity) {
        if (target instanceof Context) {
            routerCard.navigation((Context) target, requestCode, toActivity);
        } else if (target instanceof BaseViewModel) {
            ((BaseViewModel) target).navigation(routerCard, requestCode, toActivity);
        }
    }
}

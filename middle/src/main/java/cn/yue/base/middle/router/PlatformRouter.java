package cn.yue.base.middle.router;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import cn.yue.base.middle.module.IFlutterModule;
import cn.yue.base.middle.module.manager.ModuleManager;

public class PlatformRouter implements INavigation {

    private static class PlatformRouterHolder {
        private static PlatformRouter instance = new PlatformRouter();
    }

    public static PlatformRouter getInstance() {
        return PlatformRouterHolder.instance;
    }

    private INavigation navigation;
    private RouterCard routerCard;

    public synchronized RouterCard build(String pactUrl) {
        if (pactUrl.startsWith(IRouterPath.FLUTTER)) {
            navigation = ModuleManager.getModuleService(IFlutterModule.class).getFlutterRouter();
        } else if (pactUrl.startsWith(IRouterPath.NATIVE)) {
            navigation = FRouter.getInstance();
        }
        routerCard = new RouterCard(navigation);
        routerCard.setPactUrl(pactUrl);
        navigation.bindRouterCard(routerCard);
        return routerCard;
    }

    @Override
    public INavigation bindRouterCard(RouterCard routerCard) {
        this.routerCard = routerCard;
        this.routerCard.setNavigationImpl(this);
        return this;
    }

    @Override
    public void navigation(Context context) {
        if (navigation != null) {
            navigation.navigation(context);
        }
    }

    @Override
    public void navigation(@NonNull Context context, String toActivity) {
        if (navigation != null) {
            navigation.navigation(context, toActivity);
        }
    }

    public void navigation(Activity context, int requestCode) {
        if (navigation != null) {
            navigation.navigation(context, requestCode);
        }
    }

    @Override
    public void navigation(@NonNull Activity context, String toActivity, int requestCode) {
        if (navigation != null) {
            navigation.navigation(context, toActivity, requestCode);
        }
    }
}

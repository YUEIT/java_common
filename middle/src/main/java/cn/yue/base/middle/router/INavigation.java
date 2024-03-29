package cn.yue.base.middle.router;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Description : 路由跳转
 * Created by yue on 2020/4/22
 */
public interface INavigation {

    INavigation bindRouterCard(RouterCard routerCard);

    void navigation(@NonNull Object context);

    void navigation(@NonNull Object context, int requestCode);

    void navigation(@NonNull Object context, int requestCode, String toActivity);
}

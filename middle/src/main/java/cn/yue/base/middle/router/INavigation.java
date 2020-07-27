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

    void navigation(Context context);

    void navigation(@NonNull Context context, String toActivity);

    void navigation(Activity context, int requestCode);

    void navigation(@NonNull Activity context, String toActivity, int requestCode);
}

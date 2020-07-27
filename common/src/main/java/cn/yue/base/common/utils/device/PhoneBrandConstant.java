package cn.yue.base.common.utils.device;


public class PhoneBrandConstant {

    //应用启动管理 -> 关闭应用开关 -> 打开允许自启动
    public static final String HUA_WEI_PKG = "com.huawei.systemmanager";

    public static final String HUA_WEI_CLS_1 = "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity";

    public static final String HUA_WEI_CLS_2 = "com.huawei.systemmanager.optimize.bootstart.BootStartActivity";

    //授权管理 -> 自启动管理 -> 允许应用自启动
    public static final String XIAO_MI_PKG = "com.miui.securitycenter";

    public static final String XIAO_MI_CLS = "com.miui.permcenter.autostart.AutoStartManagementActivity";

    //权限隐私 -> 自启动管理 -> 允许应用自启动
    public static final String OPPO_PKG_1 = "com.coloros.phonemanager";

    public static final String OPPO_PKG_2 = "com.oppo.safe";

    public static final String OPPO_PKG_3 = "com.coloros.oppoguardelf";

    public static final String OPPO_PKG_4 = "com.coloros.safecenter";

    //权限管理 -> 自启动 -> 允许应用自启动
    public static final String VIVO_PKG = "com.iqoo.secure";

    //权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行
    public static final String MEI_ZU_PKG = "com.meizu.safe";

    //自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用
    public static final String SAMSUNG_PKG_1 = "com.samsung.android.sm_cn";

    public static final String SAMSUNG_PKG_2 = "com.samsung.android.sm";
}

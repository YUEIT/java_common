package cn.yue.base.middle.init;

import android.provider.Settings;

import cn.yue.base.common.utils.Utils;
import cn.yue.base.common.utils.device.DeviceUtils;

/**
 * Description : 初始化
 * Created by yue on 2018/11/12
 */
public class InitConstant {

    /**------------------------------------------------------------------------------------------**/
    //version.properties文件中修改对应值（正式编译版本无需修改，自动设置false）

    public static boolean isDebug;

    public static boolean logMode;

    /**------------------------------------------------------------------------------------------**/

    private static String versionName;

    public static void setVersionName(String versionName) {
        InitConstant.versionName = versionName;
    }

    public static String getVersionName() {
        return versionName;
    }

    private static String deviceId;

    public static String getDeviceId() {
        if (InitConstant.deviceId == null) {
            deviceId = DeviceUtils.getAndroidID();
        }
        return deviceId = DeviceUtils.getUniqueDeviceId(deviceId);
    }

    public final static String APP_CLIENT_TYPE = "2";

    public final static String APP_SIGN_KEY = "nK!op4w9lB.alev0";

    /**------------------------------------------------------------------------------------------**/

    private InitConstant() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

}

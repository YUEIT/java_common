package cn.yue.base.middle.init;

import cn.yue.base.common.utils.device.PhoneUtils;

/**
 * Description :
 * Created by yue on 2018/11/12
 */
public class InitConstant {

    public static boolean islogin;

    public static String loginToken;

    public static String versionName = "1.6.0";

    private static String deviceId;

    public static String getDeviceId() {
        if (InitConstant.deviceId == null) {
            deviceId = PhoneUtils.getIMEI();
        }
        return deviceId;
    }

    public final static String APP_CLIENT_TYPE = "2";

    public final static String APP_SIGN_KEY = "nK!op4w9lB.alev0";

    private InitConstant() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
}

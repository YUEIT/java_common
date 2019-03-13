package cn.yue.base.test;

import cn.yue.base.middle.net.RetrofitManager;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public class RetrofitUtils {

    private static final JavaService purchaseService = RetrofitManager.getInstance().getRetrofit("http://oc.imcoming.com").create(JavaService.class);

    public static JavaService getJavaService() {
        return purchaseService;
    }
}

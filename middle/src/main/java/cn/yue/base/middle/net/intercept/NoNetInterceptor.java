package cn.yue.base.middle.net.intercept;

import java.io.IOException;

import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import okhttp3.Interceptor;
import okhttp3.Response;


public class NoNetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtils.isNetwork()) {
            throw new ResultException(NetworkConfig.ERROR_NO_NET, "无网络:"+chain.request().url().toString());
        } else {
            return chain.proceed(chain.request());
        }
    }
}

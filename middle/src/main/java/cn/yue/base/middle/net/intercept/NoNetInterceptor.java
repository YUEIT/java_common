package cn.yue.base.middle.net.intercept;

import java.io.IOException;

import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.utils.variable.ResourceUtils;
import cn.yue.base.middle.R;
import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Description :
 * Created by yue on 2018/7/24
 */

public class NoNetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtils.isConnected()) {
            throw new ResultException(NetworkConfig.ERROR_NO_NET, ResourceUtils.getString(R.string.app_no_net));
        } else {
            return chain.proceed(chain.request());
        }
    }
}

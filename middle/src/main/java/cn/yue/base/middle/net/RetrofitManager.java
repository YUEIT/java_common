package cn.yue.base.middle.net;


import java.util.concurrent.TimeUnit;

import cn.yue.base.middle.net.gson.SignGsonConverterFactory;
import cn.yue.base.middle.net.intercept.NoNetInterceptor;
import cn.yue.base.middle.net.intercept.ResponseInterceptor;
import cn.yue.base.middle.net.intercept.SignInterceptor;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by yue on 2018/7/6.
 */

public class RetrofitManager {

    private static final int DEFAULT_TIMEOUT = 60;
    private OkHttpClient.Builder builder;

    public static RetrofitManager getInstance(){
        return RetrofitManagerHolder.instance;
    }

    private static class RetrofitManagerHolder{
        private final static RetrofitManager instance = new RetrofitManager();
    }

    private RetrofitManager() {
        builder = getOkHttpClientBuilder();
    }


    public OkHttpClient.Builder getOkHttpClientBuilder(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if (NetworkConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        builder.addInterceptor(new NoNetInterceptor())
                .addInterceptor(new SignInterceptor())
                .addInterceptor(new ResponseInterceptor());
        builder.retryOnConnectionFailure(true);
        return builder;
    }

    public Retrofit getRetrofit(String baseUrl){
        handlerError();
        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(baseUrl)
                //.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //注册自定义的工厂类
                .addConverterFactory(SignGsonConverterFactory.create())
                .build();
    }


    // 查看CallObservable的subscribeActual方法可知，一般情况下异常会被“observer.onError(t);”中处理
    // 但是如果是onError中抛出的异常，会调用RxJavaPlugins.onError方法，所有这里实现Consumer<Throwable>接口，并让异常在accept中处理
    // 考虑到ResultException是自定义异常，并不能让APP闪退，这里拦截，如果是其他异常直接抛出。
    private void handlerError(){
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if(!(throwable instanceof ResultException)){
                    throw new Exception(throwable);
                }
            }
        });
    }

}

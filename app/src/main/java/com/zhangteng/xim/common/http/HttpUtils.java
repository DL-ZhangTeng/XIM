package com.zhangteng.xim.common.http;

import android.support.annotation.NonNull;

import com.zhangteng.xim.common.config.NetConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by Lanxumit on 2017/7/27.
 */

public class HttpUtils {
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 30;
    private static HttpService httpService;

    private HttpUtils() {
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(DEFAULT_HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/json; charset=UTF-8")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(NetConfig.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    public static HttpService getInstance() {
        if (httpService == null) {
            synchronized (HttpUtils.class) {
                if (httpService == null) {
                    new HttpUtils();
                }
            }
        }
        return httpService;
    }
}

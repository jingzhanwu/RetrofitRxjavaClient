package com.jzw.dev.http.interceptor;


import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * http请求 拦截器管理工具类
 * Created by 景占午 on 2017/9/15 0015.
 */

public class InterceptorUtil {
    private static String TAG = "jzw-okhttp";
    /**
     * 返回一个管理请求头信息的拦截器
     * 这个拦截器对请求头信息进行统一的配置，无需在请求时再添加，
     * 如果请求时添加有头信息，则会覆盖这里的配置
     *
     * @return
     */
    public static Interceptor getHeadInterceptor(final Map<String, String> headMap) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Content-Type", "application/json; charset=UTF-8");
                builder.addHeader("Connection", "keep-alive"); //连接保活
                builder.addHeader("Accept", "*/*");
                builder.header("Cache-Control", String.format("public, max-age=%d", 60)); //緩存策略
                if (headMap != null && headMap.size() > 0) {
                    for (Map.Entry<String, String> map : headMap.entrySet()) {
                        builder.addHeader(map.getKey(), map.getValue());
                    }
                }
                builder.removeHeader("Pragma");

                return chain.proceed(builder.build());
            }
        };
    }

    /**
     * 日志拦截器
     *
     * @return
     */
    public static HttpLoggingInterceptor getLogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.w(TAG, "log:" + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印级别
    }
}

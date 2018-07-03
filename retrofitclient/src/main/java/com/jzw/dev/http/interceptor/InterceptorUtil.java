package com.jzw.dev.http.interceptor;


import android.text.TextUtils;
import android.util.Log;


import com.jzw.dev.http.HttpConfig;
import com.jzw.dev.http.callback.OnHttpResponseCallback;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
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
                        builder.removeHeader(map.getKey());
                        builder.addHeader(map.getKey(), map.getValue());
                    }
                }
                builder.removeHeader("Pragma");

                return chain.proceed(builder.build());
            }
        };
    }

    public static Interceptor setBaseUrlInterceptor(final String baseUrl) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //获取request的创建者builder
                Request.Builder builder = request.newBuilder();
                //从request中获取headers，通过给定的键url_name
                List<String> headerValues = request.headers(HttpConfig.BASE_URL_KEY);
                if (headerValues != null && headerValues.size() > 0) {
                    //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                    builder.removeHeader(HttpConfig.BASE_URL_KEY);
                    //匹配获得新的BaseUrl
                    String urlValue = headerValues.get(0);
                    HttpUrl newBaseUrl = null;
                    if (!TextUtils.isEmpty(urlValue)) {
                        newBaseUrl = HttpUrl.parse(urlValue);
                    } else {
                        newBaseUrl = HttpUrl.parse(baseUrl);
                    }
                    //从request中获取原有的HttpUrl实例oldHttpUrl
                    HttpUrl oldHttpUrl = request.url();
                    //重建新的HttpUrl，修改需要修改的url部分
                    HttpUrl newFullUrl = oldHttpUrl
                            .newBuilder()
                            .scheme(newBaseUrl.scheme())
                            .host(newBaseUrl.host())
                            .port(newBaseUrl.port())
                            .build();
                    //重建这个request，通过builder.url(newFullUrl).build()；
                    //然后返回一个response至此结束修改
                    return chain.proceed(builder.url(newFullUrl).build());
                } else {
                    return chain.proceed(request);
                }
            }
        };
    }


    /**
     * 响应拦截器
     *
     * @param callbacks
     * @return
     */
    public static Interceptor setOnResponseCallback(final List<OnHttpResponseCallback> callbacks) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (callbacks != null) {
                    for (OnHttpResponseCallback callback : callbacks) {
                        callback.onResponse(response.code(), null, null);
                    }
                }
                return response;
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

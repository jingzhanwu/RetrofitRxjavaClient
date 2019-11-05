package com.jzw.dev.http.interceptor;


import android.text.TextUtils;
import android.util.Log;


import com.jzw.dev.http.HttpConfig;

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
     * 网络拦截器
     *
     * @return
     */
    public static Interceptor networkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Log.w(TAG, "log:" + request.headers().toString());
                return chain.proceed(request);
            }
        };
    }

    /**
     * 返回一个管理请求头信息的拦截器
     * 这个拦截器对请求头信息进行统一的配置，无需在请求时再添加，
     * 如果请求时添加有头信息，则会覆盖这里的配置
     *
     * @param defaultHeadMap 默认头
     * @param callback
     * @return
     */
    public static Interceptor requestResponseInterceptor(final Map<String, String> defaultHeadMap, final OnInterceptorCallback callback) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();

                builder.addHeader("Content-Type", "application/json; charset=UTF-8");
                builder.addHeader("Connection", "keep-alive"); //连接保活
                builder.addHeader("Accept", "*/*");
                builder.header("Cache-Control", String.format("public, max-age=%d", 60)); //緩存策略
                builder.addHeader("Cookie", "com.lake.cn");

                //设置默认头信息
                if (defaultHeadMap != null && defaultHeadMap.size() > 0) {
                    for (Map.Entry<String, String> map : defaultHeadMap.entrySet()) {
                        builder.removeHeader(map.getKey());
                        builder.addHeader(map.getKey(), map.getValue());
                    }
                }
                builder.removeHeader("Pragma");

                Request request = builder.build();
                //如果外部有设置request拦截处理监听，则以最终设置为准
                if (callback != null) {
                    Request req = callback.onRequest(request);
                    if (req != null) {
                        request = req;
                    }
                }
                //响应结果
                Response originalResponse = chain.proceed(request);
                //如果有设置回调，则处理
                if (callback != null) {
                    originalResponse = callback.onResponse(originalResponse);
                }
                return originalResponse;
            }
        };
    }

    /**
     * 设置baseUrl设置的拦截器
     *
     * @param baseUrl
     * @return
     */
    public static Interceptor setBaseUrlInterceptor(final String baseUrl) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = setBaseUrl(chain.request(), baseUrl);
                return chain.proceed(request);
            }
        };
    }

    /**
     * 替换baseUrl的拦截器处理
     *
     * @param request
     * @param baseUrl
     * @return
     */
    private static Request setBaseUrl(Request request, String baseUrl) {
        //从request中获取headers，通过给定的键url_name
        List<String> headerValues = request.headers(HttpConfig.BASE_URL_KEY);
        if (headerValues != null && headerValues.size() > 0) {
            //获取request的创建者builder
            Request.Builder builder = request.newBuilder();
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
            builder.url(newFullUrl);

            request = builder.build();
        }
        return request;
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

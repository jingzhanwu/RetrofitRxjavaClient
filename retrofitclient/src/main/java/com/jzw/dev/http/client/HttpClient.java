package com.jzw.dev.http.client;


import android.text.TextUtils;

import com.jzw.dev.http.interceptor.InterceptorUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * 创建http客户端
 * Created by 景占午 on 2017/9/15 0015.
 */

public class HttpClient {

    private int TIME_OUT = 60;
    private String cacheDir = null;
    private boolean cookie = false;
    private Map<String, String> headMap = null;

    /**
     * 设置一些头信息，默认可以不设置，
     * 如果有额外的头需要设置，可以使用这个方法设置进来。
     * map的key是头名称，value是对应的值。
     *
     * @param head
     */
    public void setHeadMap(Map<String, String> head) {
        headMap = head;
    }

    /**
     * 设置请求连接时间，读写数据时间，响应时间
     *
     * @param time
     */
    public void setTimeOut(int time) {
        TIME_OUT = time;
    }

    /**
     * 设置请求缓存的路劲，默认没有设置
     *
     * @param filepath
     */
    public void setCacheDir(String filepath) {
        cacheDir = filepath;
    }

    /**
     * 是否支持cookie,默认没有开启
     *
     * @param open
     */
    public void supportCookie(boolean open) {
        cookie = open;
    }

    public boolean isSupportCookie() {
        return cookie;
    }

    /**
     * 正式构建一个http的客户端，这里使用的是okhttp3作为http的一个请求客户端，
     *
     * @return
     */
    public OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);

        //http缓存文件路劲
        if (!TextUtils.isEmpty(cacheDir)) {
            File cacheFile = new File(cacheDir);
            builder.cache(new Cache(cacheFile, 10 * 1024 * 1024));
        }

        if (isSupportCookie()) {
            //增加Cookie支持
            builder.cookieJar(new CookieJar() {
                //保存cookie的键值对
                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    //保存cookie
                    cookieStore.put(url, cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    //取出cookie
                    List<Cookie> cookie = cookieStore.get(url);
                    return cookie != null ? cookie : new ArrayList<Cookie>();
                }
            });
        }
        //添加一个拦截器，对请求都统一处理，这样做的好处是不用每次在ApiService的请求中配置
        builder.addInterceptor(InterceptorUtil.getHeadInterceptor(headMap));
        //添加动态修改baseUrl的拦截器
        builder.addInterceptor(InterceptorUtil.setBaseUrlInterceptor());
        //添加一个日志拦截器
        builder.addInterceptor(InterceptorUtil.getLogInterceptor());
        return builder.build();
    }
}

package com.jzw.dev.http.client;


import android.text.TextUtils;

import com.jzw.dev.http.HttpConfig;
import com.jzw.dev.http.interceptor.InterceptorUtil;
import com.jzw.dev.http.interceptor.OnInterceptorCallback;

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * 创建http客户端
 * Created by 景占午 on 2017/9/15 0015.
 */

public final class HttpClient {

    private int timeOut = 30;
    private String cacheDir = null;
    private boolean cookie = false;
    private boolean enableLog = true;
    private Map<String, String> headMap = null;

    /**
     * http客户端配置类
     */
    private HttpConfig config;
    private OnInterceptorCallback interceptorCallback;

    public HttpClient() {
        new HttpClient(new HttpConfig());
    }

    public HttpClient(HttpConfig config) {
        this.config = config;
        timeOut = config.getTimeOut();
        cacheDir = config.getCacheFile();
        cookie = config.getEnableCookie();
        enableLog = config.getEnableLog();
        headMap = config.getHeadMap();
    }

    /**
     * 设置拦截器毁掉接口
     *
     * @param interceptorCallback
     */
    public void setOnInterceptorCallback(OnInterceptorCallback interceptorCallback) {
        this.interceptorCallback = interceptorCallback;
    }

    public OnInterceptorCallback getInterceptorCallback() {
        return interceptorCallback;
    }

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
     * 是否开启日志
     *
     * @param enable
     */
    public void enableLog(boolean enable) {
        enableLog = enable;
    }

    /**
     * 设置请求连接时间，读写数据时间，响应时间
     *
     * @param time
     */
    public void setTimeOut(int time) {
        timeOut = time;
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
    public void setEnableCookie(boolean open) {
        cookie = open;
    }

    public boolean isEnableCookie() {
        return cookie;
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    /**
     * 获得一个Okhttp的 客户端
     *
     * @return
     */
    public OkHttpClient getClient() {
        return buildClient();
    }

    /**
     * 正式构建一个http的客户端，这里使用的是okhttp3作为http的一个请求客户端，
     *
     * @return
     */
    private OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeOut, TimeUnit.SECONDS);
        builder.readTimeout(timeOut, TimeUnit.SECONDS);
        builder.writeTimeout(timeOut, TimeUnit.SECONDS);

        //http缓存文件路劲
        if (!TextUtils.isEmpty(cacheDir)) {
            File cacheFile = new File(cacheDir);
            builder.cache(new Cache(cacheFile, 10 * 1024 * 1024));
        }

        if (isEnableCookie()) {
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
        builder.addInterceptor(InterceptorUtil.getHeadInterceptor(headMap, interceptorCallback));
        //添加动态修改baseUrl的拦截器
        builder.addInterceptor(InterceptorUtil.setBaseUrlInterceptor(config.getBaseUrl()));
        //添加响应你拦截器
//        builder.addInterceptor(InterceptorUtil.setOnResponseCallback(interceptorCallback));
        //添加https支持
        if (config.getHttps()) {
            SSLSocketFactory sslSocketFactory = getSSLFactory();
            if (sslSocketFactory != null) {
                builder.sslSocketFactory(sslSocketFactory);
                builder.hostnameVerifier(getHostnameVerifier());
            }
        }
        //添加一个日志拦截器
        if (isEnableLog()) {
            builder.addInterceptor(InterceptorUtil.getLogInterceptor());
        }

        return builder.build();
    }

    public HttpConfig getConfig() {
        return config;
    }


    private SSLSocketFactory getSSLFactory() {
        SSLSocketFactory sslSocketFactory;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return sslSocketFactory;
    }

    //获取TrustManager
    private TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }
                }
        };
        return trustAllCerts;
    }

    //获取HostnameVerifier
    public HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return hostnameVerifier;
    }
}

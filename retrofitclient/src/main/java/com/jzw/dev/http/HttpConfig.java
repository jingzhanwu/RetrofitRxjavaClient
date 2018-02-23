package com.jzw.dev.http;

import android.content.res.Resources;
import android.text.TextUtils;

import com.jzw.dev.http.client.HttpClient;

import java.util.Map;


/**
 * @anthor created by 景占午
 * @date 2017/11/15 0015
 * @change
 * @describe okhttp请求的统一配置参数
 **/
public class HttpConfig {
    private static HttpConfig mInstance = null;
    private static HttpClient mHttpClient = null;
    private static HttpManager mHttpManager = null;
    private static String mCacheFile = null;
    private static String mBaseUrl = "";
    private static int mTimeOut = 60;
    private static Map<String, String> mHeadMap = null;

    /**
     * 设置请求头的baseurl的key值
     */
    public final static String BASE_URL_KEY = "baseurl_key_name";

    private HttpConfig() {
    }

    /**
     * 要调用的第一个方法，初始化一个单例，
     *
     * @return
     */
    public static HttpConfig get() {
        if (mInstance == null) {
            mInstance = BuildConfig.instance;
        }
        return mInstance;
    }

    /**
     * 静态内部类，它的作用只有一个就是创建HttConfig的单利对象
     */
    static class BuildConfig {
        private final static HttpConfig instance = new HttpConfig();
    }

    /**
     * 最后调用的方法，在baseUrl，timeOut等相关属性值都已经设置好之后
     * 就可以调用这个方法正式创建出HttManager的对象，调用者就可以直接
     * 使用HttpManager对象发起请求了
     *
     * @return
     */
    public HttpConfig create() {
        if (mHttpClient == null) {
            buildHttpClient();
        }
        if (mHttpManager == null) {
            buildHttpManager();
        }
        return mInstance;
    }

    /**
     * 构建一个HttpManger对象
     */
    private void buildHttpManager() {
        mHttpManager = HttpManager.get();
        mHttpManager.init();
    }

    /**
     * 构建一个HttClient的对象，必须在设置了baseUrl之后才可调用，
     * 负责会有异常抛出
     */
    private void buildHttpClient() {
        if (!isInitConfig()) {
            new Resources.NotFoundException("not found baseUrl or not init");
            return;
        }
        mHttpClient = new HttpClient();
        mHttpClient.setTimeOut(mTimeOut);
        mHttpClient.setCacheDir(mCacheFile);
        mHttpClient.setHeadMap(mHeadMap);
    }

    public HttpClient getHttpClient() {
        return mHttpClient;
    }

    /**
     * 设置baseUrl的值，这个值是请求地址的一个基地址，最好以“/”结尾，
     * 如果是第二次调用则会判断之前有没有初始化过，如果已经初始化，则
     * 同时重新构建一次HttpMager对象
     *
     * @param url
     * @return
     */
    public HttpConfig setBaseUrl(String url) {
        mBaseUrl = url;
        if (mHttpManager != null) {
            buildHttpManager();
        }
        return mInstance;
    }

    /**
     * 设置http请求的缓存文件路劲，默认为空，可以不用设置。
     *
     * @param filePath
     * @return
     */
    public HttpConfig setCacheFile(String filePath) {
        mCacheFile = filePath;
        if (mHttpClient != null) {
            reset();
        }
        return mInstance;
    }

    /**
     * 设置请求超时时间
     *
     * @param time
     * @return
     */
    public HttpConfig setTimeOut(int time) {
        mTimeOut = time;
        if (mHttpClient != null) {
            reset();
        }
        return mInstance;
    }

    /**
     * 设置附加的头信息，key为头名称，value为对应的值，默认已经设置了请求
     * 所需要的基本头，如果还需要额外的请求头，则可以通过这个方法进行设置
     *
     * @param head
     * @return
     */
    public HttpConfig setHeadMap(Map<String, String> head) {
        mHeadMap = head;
        if (mHttpClient != null) {
            reset();
        }
        return mInstance;
    }

    /**
     * 重新配置http客户端和HttpManager的相关属性，更新对应的属性值，
     * 这种调用一般发生在，调用者临时想设置属性偏好或者更换相关属性
     * 比如（baseUrl，timeOut，headMap，cacheFile等）的值，
     * 这时才调用这个方法重新构建请求。
     */
    private void reset() {
        buildHttpClient();
        buildHttpManager();
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public String getCacheFile() {
        return mCacheFile;
    }

    public Map<String, String> getHeadMap() {
        return mHeadMap;
    }

    public int getTimeOut() {
        return mTimeOut;
    }

    /**
     * 判断是否已经初始化，必须先调用了init方法后才能做其他操作，
     * baseUrl是必须要设置的，其他的选项都有默认值，可以不用配置，
     * 所以要想创建一个http客户端就必须要有baseUrl
     *
     * @return
     */
    private boolean isInitConfig() {
        if (mInstance == null) {
            return false;
        } else if (TextUtils.isEmpty(mBaseUrl)) {
            return false;
        }
        return true;
    }
}

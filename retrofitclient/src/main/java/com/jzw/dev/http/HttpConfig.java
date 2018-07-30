package com.jzw.dev.http;


import java.util.Map;


/**
 * @anthor created by 景占午
 * @date 2017/11/15 0015
 * @change
 * @describe okhttp请求的统一配置参数
 **/
public class HttpConfig implements Cloneable {
    /*缓存文件地址*/
    private String mCacheFile = null;
    /*base Url*/
    private String mBaseUrl = "";
    /*超时时间*/
    private int mTimeOut = 60;
    /*是否打开log*/
    private boolean mEnableLog = true;
    /*是否开启cookie*/
    private boolean mCookie = false;
    /*头信息*/
    private Map<String, String> mHeadMap = null;

    /**
     * 设置请求头的baseurl的key值
     */
    public final static String BASE_URL_KEY = "baseurl_key_name";

    public HttpConfig() {
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
        this.mBaseUrl = url;
        return this;
    }

    /**
     * 是否开启日志
     *
     * @param enable
     * @return
     */
    public HttpConfig setEnableLog(boolean enable) {
        this.mEnableLog = enable;
        return this;
    }

    /**
     * 是否开启Cookie
     *
     * @param enable
     * @return
     */
    public HttpConfig setEnableCookie(boolean enable) {
        this.mCookie = enable;
        return this;
    }


    /**
     * 设置http请求的缓存文件路劲，默认为空，可以不用设置。
     *
     * @param filePath
     * @return
     */
    public HttpConfig setCacheFile(String filePath) {
        this.mCacheFile = filePath;
        return this;
    }

    /**
     * 设置请求超时时间
     *
     * @param time
     * @return
     */
    public HttpConfig setTimeOut(int time) {
        this.mTimeOut = time;
        return this;
    }

    /**
     * 设置附加的头信息，key为头名称，value为对应的值，默认已经设置了请求
     * 所需要的基本头，如果还需要额外的请求头，则可以通过这个方法进行设置
     *
     * @param head
     * @return
     */
    public HttpConfig setHeadMap(Map<String, String> head) {
        this.mHeadMap = head;
        return this;
    }

    /****************************************************************************************/
    /************************* get方法，获取设置的各个参数值***********************************/


    public String getBaseUrl() {
        return mBaseUrl;
    }

    public String getCacheFile() {
        return mCacheFile;
    }

    public boolean getEnableCookie() {
        return mCookie;
    }

    public boolean getEnableLog() {
        return mEnableLog;
    }

    public Map<String, String> getHeadMap() {
        return mHeadMap;
    }

    public int getTimeOut() {
        return mTimeOut;
    }

    @Override
    public String toString() {
        return "HttpConfig{" +
                "mCacheFile='" + mCacheFile + '\'' +
                ", mBaseUrl='" + mBaseUrl + '\'' +
                ", mTimeOut=" + mTimeOut +
                ", mEnableLog=" + mEnableLog +
                ", mCookie=" + mCookie +
                ", mHeadMap=" + mHeadMap +
                '}';
    }

    @Override
    protected Object clone() {
        HttpConfig config = null;
        try {
            config = (HttpConfig) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return config;
    }
}

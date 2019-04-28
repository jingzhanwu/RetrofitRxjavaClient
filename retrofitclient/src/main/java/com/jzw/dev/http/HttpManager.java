package com.jzw.dev.http;

import com.jzw.dev.http.callback.FileUploadObserver;
import com.jzw.dev.http.callback.OnHttpResponseCallback;
import com.jzw.dev.http.client.HttpClient;
import com.jzw.dev.http.exception.ApiException;
import com.jzw.dev.http.exception.ExceptionEngine;
import com.jzw.dev.http.callback.OnRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 初始化httpClient 装载ApiService并对外提供统一调用接口
 * 所有的http 请求都使用这个类的实例处理请求。
 * <p>
 * 使用静态内部类的形式构建一个单例
 * Created by 景占午 on 2017/9/14 0014.
 */

public class HttpManager {
    private HttpClient httpClient;
    private OkHttpClient okhttpClient;
    private OkHttpClient okHttpTempClient;
    private Retrofit.Builder retrofitBuild;
    private HttpConfig mConfig;
    private String mBaseUrl;
    private List<OnHttpResponseCallback> responseCallbacks;


    public HttpManager(HttpConfig httpConfig) {
        init(httpConfig);
        retrofitBuild = getRetrofitBuild();
    }

    /**
     * 初始化http client基本参数以及客户端实例
     *
     * @param httpConfig
     */
    private void init(HttpConfig httpConfig) {
        this.mConfig = httpConfig;
        this.mBaseUrl = mConfig.getBaseUrl();
        this.httpClient = new HttpClient(mConfig);
        this.okhttpClient = httpClient.getClient();
    }

    /**
     * 设置响应监听器
     *
     * @param callback
     * @return
     */
    public HttpManager setOnHttpResponseCallback(OnHttpResponseCallback callback) {
        if (responseCallbacks == null) {
            responseCallbacks = new ArrayList<>();
        }
        this.responseCallbacks.add(callback);
        if (httpClient != null) {
            httpClient.setOnHttpResoonseCallback(callback);
        }
        return this;
    }

    /**
     * 动态设置baseUrl，只对本次请求有效
     *
     * @param baseUrl
     * @return
     */
    public HttpManager setBaseUrl(String baseUrl) {
        this.mBaseUrl = baseUrl;
        return this;
    }

    /**
     * 设置全局头信息
     *
     * @param headMap
     * @param reset   是否覆盖之前的头
     * @return
     */
    public HttpManager setHeaders(Map<String, String> headMap, boolean reset) {
        Map<String, String> oldHead = mConfig.getHeadMap();
        if (!reset && oldHead != null && headMap != null) {
            for (Map.Entry<String, String> map : headMap.entrySet()) {
                oldHead.put(map.getKey(), map.getValue());
            }
        } else {
            oldHead = headMap;
        }
        mConfig.setHeadMap(oldHead);
        httpClient = new HttpClient(mConfig);
        okhttpClient = httpClient.getClient();
        okHttpTempClient = null;
        return this;
    }

    public HttpManager setHeaders(Map<String, String> headMap) {
        return setHeaders(headMap, false);
    }

    /**
     * 设置本次请求的临时请求头
     *
     * @param tempHeaders
     * @param resetAll    是否覆盖之前的头
     * @return
     */
    public HttpManager setLocalHeaders(Map<String, String> tempHeaders, boolean resetAll) {
        HttpConfig tempConfig = (HttpConfig) mConfig.clone();
        Map<String, String> newHeader = new HashMap<>();
        if (tempConfig.getHeadMap() != null) {
            for (Map.Entry<String, String> p : tempConfig.getHeadMap().entrySet()) {
                newHeader.put(p.getKey(), p.getValue());
            }
        }
        if (!resetAll && tempHeaders != null) {
            for (Map.Entry<String, String> map : tempHeaders.entrySet()) {
                newHeader.put(map.getKey(), map.getValue());
            }
        } else {
            newHeader = tempHeaders;
        }
        tempConfig.setHeadMap(newHeader);
        okHttpTempClient = new HttpClient(tempConfig).getClient();
        return this;
    }

    public HttpManager setLocalHeaders(Map<String, String> tempHeaders) {
        return setLocalHeaders(tempHeaders, false);
    }

    /**
     * 构造retrofit builder对象
     *
     * @return
     */
    private Retrofit.Builder getRetrofitBuild() {
        retrofitBuild = new Retrofit.Builder();
        retrofitBuild.baseUrl(mBaseUrl)
                //如果请求返回的不是json 而是字符串，则使用下面的解析器
                .addConverterFactory(ScalarsConverterFactory.create())
                //如果请求返回额是json则使用下面的解析器，
                //注意：这两句的顺序不能对调，否则不能同时兼容字符串和json
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                //添加Rxjava的支持，把Retrofit转成Rxjava可用的适配类
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return retrofitBuild;
    }

    /**
     * 构建Retrofit对象
     *
     * @return
     */
    private Retrofit getRetrofit() {
        if (retrofitBuild == null) {
            retrofitBuild = getRetrofitBuild();
        }
        retrofitBuild.client(okHttpTempClient != null ? okHttpTempClient : okhttpClient);
        Retrofit retrofit = retrofitBuild.build();

        //将url回归，有可能本次请求有设置的临时baseUrl
        mBaseUrl = mConfig.getBaseUrl();
        //临时客户端置为null
        okHttpTempClient = null;
        return retrofit;
    }

    /**
     * 获取HttpConfig对象
     *
     * @return
     */
    public HttpConfig getConfig() {
        return mConfig;
    }

    public OkHttpClient getOkhttpClient() {
        return okHttpTempClient == null ? okhttpClient : okHttpTempClient;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 获取指定的Apiservice
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getApiService(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }


    /**
     * 文件上传专用方法，可以监听上传进度
     *
     * @param observable
     * @param fileUploadObserver
     * @param <T>
     */
    public <T> void uploadFile(Observable<T> observable, FileUploadObserver<T> fileUploadObserver) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //可以处理网络状态

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);
    }

    /**
     * 网络请求对外暴露的方法，Rxjava请求都走这个方法
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public <T> void subscriber(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //可以处理网络状态

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    public <T> Disposable subscriber(final Observable<T> observable, final OnRequestListener<T> listener) {
        final Disposable disposable = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //可以处理网络状态

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                               @Override
                               public void accept(T t) throws Exception {
                                   listener.onComplete();
                                   listener.onSuccess(t);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                listener.onComplete();
                                ApiException ex = ExceptionEngine.handleException(throwable);
                                listener.onFaild(ex.getCode(), ex.getMsg());
                                dispatchResponse(ex.getCode(), ex, null);
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                listener.onComplete();
                            }
                        });

        disposable.dispose();
        return disposable;
    }

    /**
     * http响应后，将相关结果回调到调用者
     *
     * @param code
     * @param ex
     * @param response
     */
    private void dispatchResponse(int code, ApiException ex, Response response) {
        if (responseCallbacks != null) {
            for (OnHttpResponseCallback callback : responseCallbacks) {
                callback.onResponse(code, ex, response);
            }
        }
    }

    /**
     * 普通的使用 请求，返回的是Call
     *
     * @param call
     * @param listener
     * @param <T>
     */
    public <T> void request(Call<T> call, final OnRequestListener<T> listener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                listener.onComplete();
                call.cancel();
                ApiException exception = new ApiException(response.code());
                if (response.code() == 200) {
                    T body = response.body();
                    if (body != null) {
                        listener.onSuccess(body);
                    } else {
                        listener.onFaild(response.code(), exception.getMsg());
                    }
                } else {
                    listener.onFaild(exception.getCode(), exception.getMsg());
                }
                dispatchResponse(response.code(), exception, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                listener.onComplete();
                call.cancel();
                try {
                    ApiException exception = ExceptionEngine.handleException(t);
                    listener.onFaild(exception.getCode(), exception.getMsg());
                    dispatchResponse(exception.getCode(), exception, null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    listener.onFaild(2000, "未知异常");
                }
            }
        });
    }

    public Gson buildGson() {
        return new GsonBuilder().setLenient()// json宽松
                .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                .serializeNulls() //智能null,支持输出值为null的属性
                .setPrettyPrinting()//格式化输出（序列化）
                .create();
    }
}

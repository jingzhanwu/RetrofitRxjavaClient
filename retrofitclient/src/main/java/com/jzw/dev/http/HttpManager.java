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
    private static HttpManager mInstance;
    private HttpClient httpClient;
    private OkHttpClient okhttpClient;
    private OkHttpClient okHttpTempClient;
    private HttpConfig config;
    private String mBaseUrl;
    private Map<String, String> mHeadMap = null;

    private List<OnHttpResponseCallback> responseCallbacks;

    public HttpManager setOnHttpResponseCallback(OnHttpResponseCallback callback) {
        if (responseCallbacks == null) {
            responseCallbacks = new ArrayList<>();
        }
        this.responseCallbacks.add(callback);
        return this;
    }

    private HttpManager() {
    }

    /**
     * 获取本类的一个实例对象，使用DCC的模式设计单俐
     *
     * @return
     */
    public static HttpManager get() {
        if (mInstance == null) {
            synchronized (HttpManager.class) {
                if (mInstance == null) {
                    mInstance = new HttpManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * http库初始化操作，请求开始之前，
     * 一般放在application 中初始化
     *
     * @param httpConfig
     */
    public HttpManager init(HttpConfig httpConfig) {
        this.config = httpConfig;
        this.mBaseUrl = config.getBaseUrl();
        this.mHeadMap = config.getHeadMap();
        this.httpClient = new HttpClient(config);
        this.okhttpClient = httpClient.getClient();
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
     * @return
     */
    public HttpManager setHeaders(Map<String, String> headMap) {
        config.setHeadMap(headMap);
        httpClient = new HttpClient(config);
        okhttpClient = httpClient.getClient();
        okHttpTempClient = null;
        return this;
    }

    public HttpManager setLocalHeaders(Map<String, String> tempHeaders) {
        HttpConfig tempConfig = config;
        tempConfig.setHeadMap(tempHeaders);
        okHttpTempClient = new HttpClient(tempConfig).getClient();
        return this;
    }

    /**
     * 构建Retrofit对象
     *
     * @return
     */
    private Retrofit getRetrofit() {
        Retrofit.Builder rbuilder = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                //如果请求返回的不是json 而是字符串，则使用下面的解析器
                .addConverterFactory(ScalarsConverterFactory.create())
                //如果请求返回额是json则使用下面的解析器，
                //注意：这两句的顺序不能对调，否则不能同时兼容字符串和json
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                //添加Rxjava的支持，把Retrofit转成Rxjava可用的适配类
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpTempClient == null ? okhttpClient : okHttpTempClient);

        Retrofit retrofit = rbuilder.build();
        //将url回归，有可能本次请求有设置的临时baseUrl
        mBaseUrl = config.getBaseUrl();
        okHttpTempClient = null;
        return retrofit;
    }

    /**
     * 获取HttpConfig对象
     *
     * @return
     */
    public HttpConfig getConfig() {
        return config;
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
                    if (response.body() != null) {
                        listener.onSuccess(response.body());
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

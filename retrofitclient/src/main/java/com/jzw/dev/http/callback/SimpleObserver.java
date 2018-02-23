package com.jzw.dev.http.callback;

import com.jzw.dev.http.exception.ApiException;
import com.jzw.dev.http.exception.ExceptionEngine;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 默认的自定义观察者 回调接口
 * 对请求接口处理、回调
 * Created by 景占午 on 2017/9/15 0015.
 */

public abstract class SimpleObserver<T> implements Observer<T> {

    private Disposable disposable;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        try {
            ApiException exception = ExceptionEngine.handleException(e);
            onFailure(exception.getCode(), exception.getMsg());
        } catch (Exception e1) {
            e1.printStackTrace();
            onFailure(2000, "未知异常");
        }
        releaseDispose();
    }

    @Override
    public void onComplete() {
        releaseDispose();
    }

    /**
     * 取消本次订阅，
     */
    public void releaseDispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    /**
     * 子类实现，当请求成功时回掉到这里
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 子类实现，当请求失败时回掉到这里
     *
     * @param errorCode
     * @param msg
     */
    public abstract void onFailure(int errorCode, String msg);


}

package com.jzw.dev.http.callback;

import com.jzw.dev.http.exception.ApiException;
import com.jzw.dev.http.exception.ExceptionEngine;

import io.reactivex.observers.DefaultObserver;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2018/3/9 0009
 * @change
 * @describe 监听上传进度
 **/
public abstract class FileUploadObserver<T> extends DefaultObserver<T> {

    @Override
    public void onNext(T t) {
        onUploadSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        try {
            ApiException exception = ExceptionEngine.handleException(e);
            onUploadFaild(exception.getCode(), exception.getMsg());
        } catch (Exception e1) {
            e1.printStackTrace();
            onUploadFaild(2000, "未知异常");
        }

    }

    @Override
    public void onComplete() {

    }

    /**
     * 监听进度的改变
     *
     * @param byteWriten
     * @param contentLength
     */
    public void onProgressChange(long byteWriten, long contentLength) {
        float progress = (float) byteWriten / contentLength;
        onProgress((int) (progress * 100));
    }

    /**
     * 上传成功回调
     *
     * @param t
     */
    public abstract void onUploadSuccess(T t);

    /**
     * 上传失败回调
     *
     * @param
     */
    public abstract void onUploadFaild(int errorCode, String msg);

    /**
     * 上传进度回调
     *
     * @param progress
     */
    public abstract void onProgress(int progress);
}

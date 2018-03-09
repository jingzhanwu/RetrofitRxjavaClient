package com.jzw.dev.http.callback;

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
        onUploadFaild(e);
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
     * @param e
     */
    public abstract void onUploadFaild(Throwable e);

    /**
     * 上传进度回调
     *
     * @param progress
     */
    public abstract void onProgress(int progress);
}

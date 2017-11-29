package com.jzw.dev.http.callback;

import android.content.Context;

import com.jzw.dev.http.ProgressHelp;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 带progressBar的Observer
 * Created by 景占午 on 2017/9/14 0014.
 */

public abstract class ProgressObserver<T> extends SimpleObserver<T> {
    private ProgressHelp mDialog;

    public ProgressObserver(Context context) {
        mDialog = new ProgressHelp(context);
        showDialog();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        super.onSubscribe(d);
    }


    @Override
    public void onNext(@NonNull T t) {
        dismissDialog();
        super.onNext(t);
    }

    @Override
    public void onComplete() {
        dismissDialog();
        super.onComplete();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        dismissDialog();
        super.onError(e);
    }

    private void showDialog() {
        if (mDialog != null) {
            mDialog.showDialog();
        }
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismissDialog();
        }
    }
}

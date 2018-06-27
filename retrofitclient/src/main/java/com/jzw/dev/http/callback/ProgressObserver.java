package com.jzw.dev.http.callback;

import android.content.Context;

import com.jzw.dev.http.ProgressHelp;

import io.reactivex.annotations.NonNull;

/**
 * 带progressBar的Observer
 * Created by 景占午 on 2017/9/14 0014.
 */

public abstract class ProgressObserver<T> extends SimpleObserver<T> {

    public ProgressObserver(Context context) {
        ProgressHelp.get().showDialog(context);
    }

    @Override
    public void onNext(@NonNull T t) {
        ProgressHelp.get().dismissDialog();
        super.onNext(t);
    }

    @Override
    public void onComplete() {
        ProgressHelp.get().dismissDialog();
        super.onComplete();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        ProgressHelp.get().dismissDialog();
        super.onError(e);
    }
}

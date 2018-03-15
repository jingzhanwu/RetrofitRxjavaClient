package com.jzw.dev.http.callback;

import android.app.Activity;
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
        if (!isContextFinished()) {
            super.onNext(t);
        } else {
            cancel();
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        ProgressHelp.get().dismissDialog();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        ProgressHelp.get().dismissDialog();
        if (!isContextFinished()) {
            super.onError(e);
        } else {
            cancel();
        }
    }

    /**
     * 判断所依赖的context对象是否还存在
     *
     * @return
     */
    public boolean isContextFinished() {
        if (ProgressHelp.get().isShowing()) {
            return false;
        }
        Context context = ProgressHelp.get().mContext.get();
        if (context != null) {
            if (context instanceof Activity) {
                return ((Activity) context).isFinishing();
            }
        }
        return true;
    }
}

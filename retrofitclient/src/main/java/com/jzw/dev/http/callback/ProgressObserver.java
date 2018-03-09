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
    private ProgressHelp mDialog;

    public ProgressObserver(Context context) {
        mDialog = new ProgressHelp(context);
        showDialog();
    }

    @Override
    public void onNext(@NonNull T t) {
        dismissDialog();
        if (!isContextFinished()) {
            super.onNext(t);
        } else {
            cancel();
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        dismissDialog();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        dismissDialog();
        if (!isContextFinished()) {
            super.onError(e);
        } else {
            cancel();
        }
    }

    private void showDialog() {
        if (mDialog != null) {
            mDialog.showDialog();
        }
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismissDialog();
            mDialog = null;
        }
    }

    /**
     * 判断所依赖的context对象是否还存在
     *
     * @return
     */
    public boolean isContextFinished() {
        if (mDialog == null) {
            return false;
        }
        Context context = mDialog.mContext.get();
        if (context != null) {
            if (context instanceof Activity) {
                return ((Activity) context).isFinishing();
            }
        }
        return true;
    }
}

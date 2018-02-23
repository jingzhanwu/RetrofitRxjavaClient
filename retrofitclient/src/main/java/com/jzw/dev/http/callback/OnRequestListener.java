package com.jzw.dev.http.callback;


import android.app.Activity;
import android.content.Context;

import com.jzw.dev.http.ProgressHelp;

/**
 * @anthor created by 景占午
 * @date 2017/11/9 0009
 * @change
 * @describe 回掉接口
 **/
public abstract class OnRequestListener<T> {
    private ProgressHelp mDialog;
    /**
     * 一般情况下使用的构造函数，
     */
    public OnRequestListener() {
    }

    /**
     * 带一个参数的构造函数，当调用者需要显示一个dialog的时候可以
     * 使用这个构造函数，传递一个context对象，就可以自动显示
     * 一个dialog，而且是自定管理显示和隐藏。
     *
     * @param context
     */
    public OnRequestListener(Context context) {
        mDialog = new ProgressHelp(context);
        onStart();
    }

    /**
     * 请求之前做一些处理，这里就直接显示一个dialog了，
     * 调用者也可以自定义个接口或者抽象类来集成这类，实现自己的处理逻辑
     */
    public void onStart() {
        if (mDialog != null) {
            mDialog.showDialog();
        }
    }

    /**
     * 请求完成的时候调用，做一些善后处理
     */
    public void onComplete() {
        if (mDialog != null) {
            mDialog.dismissDialog();
            mDialog = null;
        }
    }

    /**
     * 抽象方法，子类去实现，当请求成功的时候回掉
     * * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 请求失败时回掉
     *
     * @param code
     * @param msg
     */
    public abstract void onFaild(int code, String msg);

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

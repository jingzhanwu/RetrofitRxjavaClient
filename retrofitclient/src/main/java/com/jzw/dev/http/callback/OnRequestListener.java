package com.jzw.dev.http.callback;


import android.content.Context;

import com.jzw.dev.http.ProgressHelp;

/**
 * @anthor created by 景占午
 * @date 2017/11/9 0009
 * @change
 * @describe 回掉接口
 **/
public  abstract class OnRequestListener<T> {
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
        ProgressHelp.get().showDialog(context);
    }


    /**
     * 请求完成的时候调用，做一些善后处理
     */
    public void onComplete() {
        ProgressHelp.get().dismissDialog();
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
}

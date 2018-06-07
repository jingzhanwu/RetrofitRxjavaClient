package com.jzw.dev.http.exception;

import android.text.TextUtils;

/**
 * @anthor created by 景占午
 * @date 2017/11/15 0015
 * @change
 * @describe 自定义异常处理的类，对常见的一些一场同一处理
 * 给用户一个友好的提示
 **/
public class ApiException extends Exception {
    private int code;//错误码
    private String msg;//错误信息

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        handleMsg();
    }

    public ApiException(int code) {
        this.code = code;
        handleMsg();
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void handleMsg() {
        switch (code) {
            case 400:
                setMsg("错误请求");
                break;
            case 401:
                setMsg("身份认证失败");
                break;
            case 404:
                setMsg("地址无效");
                break;
            case 405:
                setMsg("禁止访问");
                break;
            case 407:
                setMsg("需要代理身份验证");
                break;
            case 408:
                setMsg("请求超时");
                break;
            case 414:
                setMsg("请求地址太长");
                break;
            case 500:
                setMsg("服务器出错了");
                break;
            case 501:
                setMsg("不支持此请求");
                break;
            case 502:
                setMsg("网关出错");
                break;
            case 504:
                setMsg("服务器超时");
                break;

            case 1000:
                setMsg("服务器超时");
                break;
            case 1001:
                setMsg("解析出错了");
                break;
            case 1002:
                setMsg("请检查网络连接");
                break;
            case 1003:
                setMsg("网络超时");
                break;
        }
        if (TextUtils.isEmpty(msg)) {
            setMsg("服务器错误");
        }
    }
}

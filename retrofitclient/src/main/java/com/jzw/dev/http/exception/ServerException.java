package com.jzw.dev.http.exception;

/**
 * @anthor created by Administrator
 * @date 2017/11/15 0015
 * @change
 * @describe describe
 **/
public class ServerException extends RuntimeException {
    private int code;
    private String msg;

    public ServerException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

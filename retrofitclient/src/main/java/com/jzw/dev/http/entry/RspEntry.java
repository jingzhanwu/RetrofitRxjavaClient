package com.jzw.dev.http.entry;


/**
 * @anthor created by 景占午
 * @change
 * @describe 请求返回结果的抽象父类，定义了大多时候的返回结果json结构，
 * 如果调用者的json结构和定义的一样，则直接集成这个类，添加未实现的属性即可，
 * 如果不一样则调用者自行定义
 **/

public class RspEntry {

    private int code;
    private String message;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

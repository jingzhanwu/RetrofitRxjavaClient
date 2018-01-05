package com.jzw.retrofit.base;

import com.jzw.dev.http.entry.RspEntry;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2017/11/28 0028
 * @change
 * @describe 接口请求基类
 **/
public class ResultBase<T> extends RspEntry {

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private T data;

}

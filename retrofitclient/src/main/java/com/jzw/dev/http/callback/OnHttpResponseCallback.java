package com.jzw.dev.http.callback;

import com.jzw.dev.http.exception.ApiException;

import retrofit2.Response;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2018/6/27 0027
 * @change
 * @describe 监听http的响应
 **/
public interface OnHttpResponseCallback {
    public void onResponse(int code, ApiException ex, Response response);
}

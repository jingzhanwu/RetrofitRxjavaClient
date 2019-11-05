package com.jzw.dev.http.callback;


import com.jzw.dev.http.exception.ApiException;

import retrofit2.Response;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2019/11/5
 * @change
 * @describe 请求响应内容拦截器回调
 **/
public interface OnResponseInterceptorCallback {

    Response onResponse(int code, ApiException ex, Response response);
}

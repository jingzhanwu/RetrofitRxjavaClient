package com.jzw.dev.http.interceptor;

import com.jzw.dev.http.exception.ApiException;

import okhttp3.Request;
import retrofit2.Response;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2019/8/28 0028
 * @change
 * @describe describe
 **/
public interface OnInterceptorCallback {
    Request onRequest(Request request);

    void onResponse(int code, ApiException ex, Response response);
}

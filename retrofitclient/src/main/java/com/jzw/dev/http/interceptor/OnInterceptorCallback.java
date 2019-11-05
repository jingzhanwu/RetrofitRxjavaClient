package com.jzw.dev.http.interceptor;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2019/8/28 0028
 * @change
 * @describe 请求 详情拦截器回调接口
 **/
public interface OnInterceptorCallback {
    Request onRequest(Request request);

    Response onResponse(Response response);
}
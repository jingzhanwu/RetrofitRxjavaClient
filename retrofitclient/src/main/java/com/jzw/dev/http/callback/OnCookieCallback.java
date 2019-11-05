package com.jzw.dev.http.callback;

import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jzw
 * @date 2019/11/5
 * @change
 * @describe http cookie设置与保存的回调接口
 **/
public interface OnCookieCallback {
    /**
     * 响应返回的cookie
     *
     * @param url
     * @param cookies
     * @param cookieStore 已经保存的cookie Map
     */
    void onSaveCookie(HttpUrl url, List<Cookie> cookies, HashMap<String, List<Cookie>> cookieStore);

    /**
     * 要设置的cookie
     *
     * @param url
     * @param savedCookie 已经保存的cookie，不设置则使用此值
     * @return 设置的结果
     */
    List<Cookie> onSetCookie(HttpUrl url,List<Cookie> savedCookie);
}

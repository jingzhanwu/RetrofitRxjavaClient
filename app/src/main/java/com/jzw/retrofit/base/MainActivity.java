package com.jzw.retrofit.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jzw.dev.http.HttpConfig;
import com.jzw.dev.http.HttpManager;
import com.jzw.dev.http.callback.OnCookieCallback;
import com.jzw.dev.http.callback.OnRequestListener;
import com.jzw.dev.http.exception.ApiException;
import com.jzw.dev.http.interceptor.OnInterceptorCallback;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private String mToken = "388797140820045824";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String token = "token";
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", mToken);

                HttpConfig config = new HttpConfig();
                config.setBaseUrl("https://apidev.lakeapp.cn/");
                config.setEnableLog(true);
                config.setHttps(true);
                config.setEnableCookie(true);
                config.setEnableNetworkInterceptor(true);
                config.setHeadMap(map);

                HttpManager.Get().init(config);

                HttpManager.Get().setOnInterceptorCallback(new OnInterceptorCallback() {
                    @Override
                    public Request onRequest(Request request) {
                        String token = request.header("Authorization");
                        System.out.println("设置的token：" + token);
//                        Request.Builder builder = request.newBuilder();
//                        builder.removeHeader("Authorization");
//                        builder.addHeader("Authorization", "155555555555");
//
//                        request = builder.build();
                        return request;
                    }

                    @Override
                    public Response onResponse(Response response) {
                        return response;
                    }
                });

                HttpManager.Get().setOnCookieCallback(new OnCookieCallback() {
                    @Override
                    public void onSaveCookie(HttpUrl url, List<Cookie> cookies, HashMap<String, List<Cookie>> cookieStore) {
                        System.out.println("监听cookie：" + cookies);
                    }

                    @Override
                    public List<Cookie> onSetCookie(HttpUrl url, List<Cookie> savedCookie) {
                        if (savedCookie == null) {
                            savedCookie = new ArrayList<>();
                        }
                        System.out.println("监听设置cookie：" + url);

                        Cookie cookie = new Cookie.Builder()
                                .hostOnlyDomain(url.host())
                                .path(url.encodedPath())
                                .name("jzw")
                                .value("com.lake.mgr.cn")
                                .build();

                        savedCookie.add(cookie);
                        return savedCookie;
                    }
                });
                get();
            }
        });

    }


    private void get() {
        Call<String> call = HttpManager.Get().getApiService(ApiService.class).banding();
        HttpManager.Get().request(call, new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFaild(int code, String msg) {
            }
        });
    }

}


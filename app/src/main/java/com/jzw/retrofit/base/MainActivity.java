package com.jzw.retrofit.base;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jzw.dev.http.HttpConfig;
import com.jzw.dev.http.HttpManager;
import com.jzw.dev.http.callback.OnCookieCallback;
import com.jzw.dev.http.callback.OnRequestListener;
import com.jzw.dev.http.interceptor.OnInterceptorCallback;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
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
                //http://10.168.4.41/
                //https://10.168.4.41:80/
                //https://apidev.lakeapp.cn/
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
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
//                getAllMap();
            }
        });
    }

    /**
     * 模拟登录
     */
    private void login() {
        JSONObject json = new JSONObject();
        try {
            json.put("clientType", "android");
            json.put("loginName", "jingzhanwu");
            json.put("password", "123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Call<String> call = HttpManager.Get().getApiService(ApiService.class)
                .login(body);
        HttpManager.Get().request(call, new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFaild(int code, String msg) {

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

    private void getAllMap() {
        Call<String> call = HttpManager.Get().getApiService(ApiService.class).getAllMap();
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


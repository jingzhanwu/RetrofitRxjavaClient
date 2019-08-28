package com.jzw.retrofit.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jzw.dev.http.HttpConfig;
import com.jzw.dev.http.HttpManager;
import com.jzw.dev.http.callback.OnRequestListener;
import com.jzw.dev.http.exception.ApiException;
import com.jzw.dev.http.interceptor.OnInterceptorCallback;


import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private String mToken = "363786482777075712";

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
                config.setHeadMap(map);

                HttpManager.Get().init(config);

                HttpManager.Get().setOnInterceptorCallback(new OnInterceptorCallback() {
                    @Override
                    public Request onRequest(Request request) {
                        String token = request.header("Authorization");
                        System.out.println("设置的token：" + token);
                        Request.Builder builder = request.newBuilder();
                        builder.removeHeader("Authorization");
                        builder.addHeader("Authorization", "155555555555");

                        request = builder.build();
                        return request;
                    }

                    @Override
                    public void onResponse(int code, ApiException ex, retrofit2.Response response) {
                        System.out.println("响应code：" + code + "原始code:" + response.code());
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
                System.out.println("结果：" + s);
            }

            @Override
            public void onFaild(int code, String msg) {

                System.out.println("错误：" + msg);
            }
        });
    }

}


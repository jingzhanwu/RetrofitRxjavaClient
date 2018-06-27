package com.jzw.retrofit.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jzw.dev.http.HttpConfig;
import com.jzw.dev.http.HttpManager;
import com.jzw.dev.http.ProgressHelp;
import com.jzw.dev.http.callback.OnHttpResponseCallback;
import com.jzw.dev.http.callback.OnRequestListener;
import com.jzw.dev.http.callback.ProgressObserver;
import com.jzw.dev.http.callback.SimpleObserver;
import com.jzw.dev.http.exception.ApiException;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json;charset=UTF-8");

        HttpManager.get().init(new HttpConfig()
                .setBaseUrl("http://192.168.0.100:8080/")
                .setTimeOut(60)
                .setHeadMap(map))
                .setOnHttpResponseCallback(new OnHttpResponseCallback() {
                    @Override
                    public void onResponse(int code, ApiException ex, Response response) {

                    }
                });

        //requestTest();
        //uploadFile();
    }

    public void testHttp(View v) {

    }

    /**
     * 普通请求的几种方式
     */
    public void requestTest() {
        /**
         * 返回值是Observable的集中使用方法
         */

        Observable<String> observable = HttpManager.get().getApiService(ApiService.class).testRxjava();
        HttpManager.get().subscriber(observable, new SimpleObserver<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure(int errorCode, String msg) {

            }
        });
        //带进度条
        HttpManager.get().subscriber(observable, new ProgressObserver<String>(this) {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure(int errorCode, String msg) {

            }
        });

        /**
         * rxjava2 中使用 Consumer的用法，只是回掉不一样
         */
        HttpManager.get().subscriber(observable, new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFaild(int code, String msg) {

            }
        });

        //带进度条
        HttpManager.get().subscriber(observable, new OnRequestListener<String>(this) {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFaild(int code, String msg) {

            }
        });

        /**
         * 普通的请求，不适用rxjava的方式
         */
        Call<String> call = HttpManager.get().getApiService(ApiService.class).testRetrofit();
        HttpManager.get()
                .setBaseUrl("http://10.1.1.1:8080/")
                .setHeaders(null)
                .setLocalHeaders(null)
                .request(call, new OnRequestListener<String>(this) {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onFaild(int code, String msg) {

                    }
                });

    }
}


package com.jzw.retrofit.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = "";
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", token);

                HttpConfig config = new HttpConfig();
                config.setBaseUrl("http://192.168.....");
                config.setEnableLog(true);
                config.setHeadMap(map);

                HttpManager.get().init(config);
            }
        });

        //普通请求
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });

        //设置临时头
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHead();
            }
        });

        //requestTest();
        //uploadFile();
    }

    public void setHead() {
        Map<String, String> map = new HashMap<>();
        map.put("User-Agent", "jingzhanwu");

        Call<Object> call = HttpManager.get()
                .setBaseUrl("http://192.168.20.89")
                .setLocalHeaders(map)
                .getApiService(ApiService.class).getDispatchCount();

        HttpManager.get().request(call, new OnRequestListener<Object>() {
            @Override
            public void onSuccess(Object o) {
                System.out.println("成功了》》" + o.toString());
            }

            @Override
            public void onFaild(int code, String msg) {
                System.out.println("失败了》》" + msg);
            }
        });
    }


    public void request() {
        Call<Object> call = HttpManager.get()
                .getApiService(ApiService.class).getDispatchCountGroup();
        HttpManager.get().request(call, new OnRequestListener<Object>() {
            @Override
            public void onSuccess(Object o) {
                System.out.println("成功了》》" + o.toString());
            }

            @Override
            public void onFaild(int code, String msg) {
                System.out.println("失败了》》" + msg);
            }
        });
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


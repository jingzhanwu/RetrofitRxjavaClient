package com.jzw.retrofit.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jzw.dev.http.HttpConfig;
import com.jzw.dev.http.HttpManager;
import com.jzw.dev.http.callback.OnRequestListener;
import com.jzw.dev.http.callback.ProgressObserver;
import com.jzw.dev.http.callback.SimpleObserver;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private HttpManager httpManager;

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4dWViaW5nIiwianRpIjoiZGFvc2h1IiwiaWF0IjoxNTU2MjYwNjM3LCJpc3MiOiJodHRwOi8vZGFvc2h1LmNvbSJ9.7KEmyf6vHLNbrZ6LNr8k9dW-KmiOH6UnqP1fabvTLYyupUJ77HbEXixHeoiGaYSqxF9oOasExHpj5N6lQAOmmQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String token = "token";
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", token);

                HttpConfig config = new HttpConfig();
                config.setBaseUrl("http://10.168.31.223:9051/");
                config.setEnableLog(true);
                config.setHeadMap(map);

                httpManager = new HttpManager(config);
            }
        });

        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryMicroGroups(false);
            }
        });

    }


    /**
     * 查询微群列表
     *
     * @param isSearch
     */
    public void queryMicroGroups(boolean isSearch) {
        JSONObject json = new JSONObject();
        if (isSearch) {
            try {
                json.put("title", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Call<Rsp<MicroGroup>> call = httpManager.getApiService(ApiService.class)
                .queryMicroGroupList(body);
        httpManager.request(call, new OnRequestListener<Rsp<MicroGroup>>() {
            @Override
            public void onSuccess(Rsp<MicroGroup> mg) {

            }

            @Override
            public void onFaild(int code, String msg) {

            }
        });
    }


    public void setHead() {
        Map<String, String> map = new HashMap<>();
        map.put("User-Agent", "jingzhanwu");

        Call<Object> call = httpManager
                .setBaseUrl("http://192.168.20.89")
                .setLocalHeaders(map)
                .getApiService(ApiService.class).getDispatchCount();

        httpManager.request(call, new OnRequestListener<Object>() {
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
        Call<Object> call = httpManager
                .getApiService(ApiService.class).getDispatchCountGroup();
        httpManager.request(call, new OnRequestListener<Object>() {
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

        Observable<String> observable = httpManager.getApiService(ApiService.class).testRxjava();
        httpManager.subscriber(observable, new SimpleObserver<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure(int errorCode, String msg) {

            }
        });
        //带进度条
        httpManager.subscriber(observable, new ProgressObserver<String>(this) {
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
        httpManager.subscriber(observable, new OnRequestListener<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFaild(int code, String msg) {

            }
        });

        //带进度条
        httpManager.subscriber(observable, new OnRequestListener<String>(this) {
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
        Call<String> call = httpManager.getApiService(ApiService.class).testRetrofit();
        httpManager
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


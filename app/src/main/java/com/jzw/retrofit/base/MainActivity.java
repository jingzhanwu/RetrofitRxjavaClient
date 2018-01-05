package com.jzw.retrofit.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jzw.dev.http.HttpConfig;
import com.jzw.dev.http.HttpManager;
import com.jzw.dev.http.callback.OnRequestListener;
import com.jzw.dev.http.callback.ProgressObserver;
import com.jzw.dev.http.callback.SimpleObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, String> map = new HashMap<>();
         map.put("Content-Type", "application/json;charset=UTF-8");

        HttpConfig.init()
                .setBaseUrl("http://...")
                .setTimeOut(10)
                .setHeadMap(map)
                .create();

        // requestTest();
        //uploadFile();

        //带进度条
//
//        JSONObject json=new JSONObject();
//        try {
//            json.put("start","1");
//            json.put("size","20");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        RequestBody body=RequestBody.create(MediaType.parse("application/json"),json.toString());
//        Call<ResultBase<PoliceInfo>> observable = HttpManager.get().getApiService(ApiService.class).getPoliceList(body);
//
//        HttpManager.get().request(observable, new OnRequestListener<ResultBase<PoliceInfo>>() {
//            @Override
//            public void onSuccess(ResultBase<PoliceInfo> s) {
//                System.out.println("成功》" + s);
//            }
//
//            @Override
//            public void onFaild(int code, String msg) {
//                System.out.println("失败》" + code + ">>" + msg);
//            }
//        });
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
        HttpManager.get().request(call, new OnRequestListener<String>(this) {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFaild(int code, String msg) {

            }
        });

    }

    /**
     * 文件上传测试
     */
    public void uploadFile() {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "");
        params.put("param2", "");

        List<File> files = new ArrayList<>();
        files.add(new File(""));
        files.add(new File(""));
        //第一种返回 一个MultipartBody.Part集合
        List<MultipartBody.Part> part = HttpManager.get().buildMultPartList(params, files, "file");

        HttpManager.get().getApiService(ApiService.class).uploadFile(part);
        //第二种返回一个MultipartBody
        MultipartBody body = HttpManager.get().buildMultipartBody(params, files, "file");
        Observable<String> observable = HttpManager.get().getApiService(ApiService.class).uploadFile2(body);

        //以上两种方式都可以进行多文件 + 普通参数的形式上传文件

        HttpManager.get().subscriber(observable, new ProgressObserver<String>(this) {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure(int errorCode, String msg) {

            }
        });
    }

}


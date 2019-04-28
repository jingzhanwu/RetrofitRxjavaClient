package com.jzw.dev.http.exception;

import android.util.MalformedJsonException;


import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by Administrator
 * @date 2017/11/15 0015
 * @change
 * @describe describe
 **/
public class ExceptionEngine {
    public static final int UN_KNOWN_ERROR = 1000;//未知错误
    public static final int ANALYTIC_SERVER_DATA_ERROR = 1001;//解析(服务器)数据错误
    public static final int CONNECT_ERROR = 1002;//网络连接错误
    public static final int TIME_OUT_ERROR = 1003;//网络连接超时

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpExc = (HttpException) e;
            ex = new ApiException(e, httpExc.code());
            return ex;
        } else if (e instanceof ServerException) {
            //服务器返回的错误
            ServerException serverExc = (ServerException) e;
            ex = new ApiException(serverExc, serverExc.getCode());
            return ex;
        }
        else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {  //解析数据错误
            ex = new ApiException(e, ANALYTIC_SERVER_DATA_ERROR);
            ex.setMsg("数据解析错误");
            return ex;
        }
        else if (e instanceof ConnectException) {//连接网络错误
            ex = new ApiException(e, CONNECT_ERROR);
            ex.setMsg("请检查网络连接");
            return ex;
        } else if (e instanceof SocketTimeoutException) {//网络超时
            ex = new ApiException(e, TIME_OUT_ERROR);
            ex.setMsg("网络超时");
            return ex;
        } else {  //未知错误
            ex = new ApiException(e, UN_KNOWN_ERROR);
            ex.setMsg("出错了，请重试");
            return ex;
        }
    }
}

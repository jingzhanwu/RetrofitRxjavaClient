package com.jzw.retrofit.base;


import java.util.List;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @anthor created by Administrator
 * @date 2017/11/15 0015
 * @change
 * @describe describe
 **/
public interface ApiService {

    @GET("cms/content/usingHelp")
    Call<String> testRetrofit();

    @GET("cms/content/usingHelp")
    Observable<String> testRxjava();


    @Multipart
    @POST("")
    Observable<String> uploadFile(@Part List<MultipartBody.Part> partList);

    @POST("")
    Observable<String> uploadFile2(@Body MultipartBody body);
}

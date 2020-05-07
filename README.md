# RetrofitRxjavaClient

### 这是一个使用Retrofit2 + Rxjava2封装的一个简易型的网络请求库,
**1. 支持https**

**2. 支持Cookie**

**3. 支持Androidx**

  如何引入：
  compile 'com.jzw.net:http:2.8.3' 引入即可。

  Androidx版本
  compile 'com.jzw.net:http:3.0.0' 引入即可。
 

 代码中如何使用：

 1.初始化配置
       HttpManager.Get().init(new HttpConfig()
                                  .setBaseUrl("http://192.168.0.100:8080/")
                                   .setTimeOut(60));
       如果需要额外配置头信息，或者自定义超时时间，只需要在后面加上即可
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "");
        map.put("user-agent", "android");
             
       HttpManager.Get().init(new HttpConfig()
                                  .setBaseUrl("http://192.168.0.100:8080/")
                                  .setTimeOut(60)
                                  .setHttps(true)
                                  .setHeadMap(map));

        设置请求响应监听拦截器器，处理特殊的响应结果
        HttpManager.Get().setOnResponseInterceptorCallback(new OnResponseInterceptorCallback() {
                            @Override
                            public void onResponse(int code, ApiException ex, Response response) {

                            }
                     });

    支持动态配置baseUrl，请求头，在接口调用时设置
      BaseUrl设置
      HttpManager.Get().setBaseUrl().request....

      全局请求头变更：
      HttpManager.Get().setHeaders()

      临时请求头变更（当次请求有效）
      HttpManager.Get().setLocalHeaders()

    
 2.调用
  
     首页定义ApiService: 里面定义请求接口，具体的定义可查阅Rest full 接口规范，例如：
     
      //post 请求
       @POST("/message/message/batchUpdateReaded")
       Call<ResultObj> updateNoticeStatus(@Query("msgIds") String ids);
  
        //get 请求
     @GET("/message/message/findUserMsgCount")
        Call<ResultObj<String>> findMessageCount(@Query("userId") String userId,
                                                 @Query("readStatus") String status,
                                                 @Query("type") String type);
      

    （1）配合rxjava方式

           /**
            * 返回值是Observable 不带progressBar
            */
           Observable<String> observable = HttpManager.Get().getApiService(ApiService.class).testRxjava();
           HttpManager.Get().subscriber(observable, new SimpleObserver<String>() {
               @Override
               public void onSuccess(String s) {

               }

               @Override
               public void onFailure(int errorCode, String msg) {

               }
           });

         /**
           * 返回值是Observable，带progressBar
           */
          Observable<String> observable = HttpManager.Get().getApiService(ApiService.class).testRxjava();
          HttpManager.Get().subscriber(observable, new SimpleObserver<String>(this) {
              @Override
              public void onSuccess(String s) {

              }

              @Override
              public void onFailure(int errorCode, String msg) {

              }
          });

    （2）普通的retrofit使用方式
          /**
             * 普通的请求，返回Call，带progressBar，不带progressBar的使用同上，
             * 不传context即可
             */
            Call<String> call = HttpManager.Get().getApiService(ApiService.class).testRetrofit();
            HttpManager.Get().request(call, new OnRequestListener<String>(this) {
                @Override
                public void onSuccess(String s) {

                }

                @Override
                public void onFaild(int code, String msg) {

                }
            });

    （3）带文件上传的请求
         Map<String, String> params = new HashMap<>();
            params.put("param1", "");
            params.put("param2", "");

            List<File> files = new ArrayList<>();
            files.add(new File(""));
            files.add(new File(""));
            //第一种，返回 一个MultipartBody.Part集合
            List<MultipartBody.Part> part = new ApiParams().buildMultPartList(params, files, "file");

            HttpManager.Get().getApiService(ApiService.class).uploadFile(part);
            //第二种，返回一个MultipartBody
            MultipartBody body = HttpManager.Get().buildMultipartBody(params, files, "file");
            Observable<String> observable = HttpManager.get().getApiService(ApiService.class).uploadFile2(body);

            //以上两种方式都可以进行多文件 + 普通参数的形式上传文件

            HttpManager.Get().subscriber(observable, new ProgressObserver<String>(this) {
                @Override
                public void onSuccess(String s) {

                }

                @Override
                public void onFailure(int errorCode, String msg) {

                }
            });
            
    (4) 带进度监听的文件上传，支持多文件 + 文本混合上传
        FileUploadObserver<ResultObj> fileUploadObserver = new FileUploadObserver<ResultObj>() {
                    @Override
                    public void onUploadSuccess(ResultObj result) {
                        System.out.println("成功》》" + result.getCode());
                    }
        
                    @Override
                    public void onUploadFaild(Throwable e) {
                        System.out.println("失败》》" + e.getMessage());
                    }
        
                    @Override
                    public void onProgress(int progress) {
        
                        System.out.println("进度》》" + progress);
                    }
                };
        
                RequestBody body = new ApiParams().buildMultipartBodys(files, "uploadFile");
                UploadFileRequestBody fileBody = new UploadFileRequestBody(body, fileUploadObserver);
                Observable<ResultObj> observable = HttpManager.get().getApiService(ApiFileService.class).upload(fileBody);
        
                HttpManager.Get().subscriber(observable, fileUploadObserver);
            
  博客地址：http://my.csdn.net/qq_19979101
  
  Mvp开发框架
  https://github.com/jingzhanwu/MvpBase
  
  android工具库
  https://github.com/jingzhanwu/DevUtils
  
  Android 多媒体库
  https://github.com/jingzhanwu/MediaLibrary
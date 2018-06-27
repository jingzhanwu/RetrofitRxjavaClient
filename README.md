# RetrofitRxjavaClient

这是一个使用Retrofit2 + Rxjava2 + okhttp3封装的一个简易型的网络请求库，
第一次在项目上使用这些新技术，写的不好，还希望大家多提宝贵意见。

  如何引入：
  compile 'com.jzw:http-retrofit:2.6.0' 引入即可。
 

 代码中如何使用：

 1.初始化配置
 
       HttpManager.get().init(new HttpConfig()
                             .setBaseUrl("http://192.168.0.100:8080/")
                             .setTimeOut(60)
                             .setHeadMap(map)));

       如果需要额外配置头信息，或者自定义超时时间，只需要在后面加上即可
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "");
        map.put("user-agent", "android");

        HttpManager.get().init(new HttpConfig()
             .setBaseUrl("http://192.168.0.100:8080/")
             .setTimeOut(60)
             .setHeadMap(map)));


        设置请求响应监听器，处理特殊的响应结果
         HttpManager.get().init(new HttpConfig()
                     .setBaseUrl("http://192.168.0.100:8080/")
                     .setTimeOut(60)
                     .setHeadMap(map))
                     .setOnHttpResponseCallback(new OnHttpResponseCallback() {
                            @Override
                            public void onResponse(int code, ApiException ex, Response response) {

                            }
                     });

    支持动态配置baseUrl，请求头，在接口调用时设置
      BaseUrl设置
      HttpManager.get().setBaseUrl().request....

      全局请求头变更：
      HttpManager.get().setHeaders()

      临时请求头变更（当次请求有效）
      HttpManager.get().setLocalHeaders()

    
 2.调用

    （1）配合rxjava方式

           /**
            * 返回值是Observable 不带progressBar
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

         /**
           * 返回值是Observable，带progressBar
           */
          Observable<String> observable = HttpManager.get().getApiService(ApiService.class).testRxjava();
          HttpManager.get().subscriber(observable, new SimpleObserver<String>(this) {
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
            Call<String> call = HttpManager.get().getApiService(ApiService.class).testRetrofit();
            HttpManager.get().request(call, new OnRequestListener<String>(this) {
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

            HttpManager.get().getApiService(ApiService.class).uploadFile(part);
            //第二种，返回一个MultipartBody
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
        
                HttpManager.get().subscriber(observable, fileUploadObserver);
            
  博客地址：http://my.csdn.net/qq_19979101
  
  Mvp开发框架
  https://github.com/jingzhanwu/MvpBase
  
  android工具库
  https://github.com/jingzhanwu/DevUtils
  
  Android 多媒体库
  https://github.com/jingzhanwu/MediaLibrary
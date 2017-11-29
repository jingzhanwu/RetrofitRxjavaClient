# RetrofitClient
这是一个使用Retrofit2 + Rxjava2 + Rxandroid + okhttp3封装的一个简易型的网络请求库，第一次在项目上使用这些新技术，写的不好，还希望大家多提宝贵意见。

 如何引入：
 
  1.MavenCenter方式引入，在project下的build.gradle配置如下
  
    allprojects {
      repositories {
        ...
        maven {
            url 'https://dl.bintray.com/jingzhanwu/jzw/'
        }
      }
    }

 compile 'com.jzw.net:retrofit:1.2.0' 引入即可。
 
 2.jcenter方式
 
 compile 'com.jzw.net:retrofit:1.2.0' 直接引入即可。

 代码中如何使用：

 1.全局只需要初始化配置一次
     HttpConfig.init()
       .setBaseUrl("http://...")
       .create();

       如果需要额外配置头信息，或者自定义超时时间，只需要在后面加上即可
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "");
        map.put("user-agent", "android");

        HttpConfig.init()
                .setBaseUrl("http://...")
                .setTimeOut(30)
                .setHeadMap(map)
                .create();

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
            List<MultipartBody.Part> part = HttpManager.get().buildMultPartList(params, files, "file");

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
            
  博客地址：http://my.csdn.net/qq_19979101
  Mvp开发框架
  https://github.com/jingzhanwu/MvpBase
  android工具库
  https://github.com/jingzhanwu/DevUtils
  
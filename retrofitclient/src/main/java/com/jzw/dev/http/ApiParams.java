package com.jzw.dev.http;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @anthor created by jingzhanwu
 * @date 2018/2/23 0023
 * @change
 * @describe 参数构造
 **/
public class ApiParams {


    /**
     * 构建单文件的part
     * APi 接口中必须使用@Multipart 注解
     *
     * @param file
     * @param fileFlag
     * @return
     */
    public MultipartBody.Part buildSigleMultipart(File file, String fileFlag) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(fileFlag, file.getName(), requestBody);
        return part;
    }

    /**
     * 构造多文件上传的part List
     * APi 接口中必须使用@Multipart 注解
     *
     * @param files
     * @param fileFlag
     * @return
     */
    public List<MultipartBody.Part> buildMultiParts(List<File> files, String fileFlag) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (File file : files) {
            parts.add(buildSigleMultipart(file, fileFlag));
        }
        return parts;
    }

    /**
     * 构建文本参数和 多文件同时上传的 part list
     * APi 接口中必须使用@Multipart 注解
     *
     * @param params
     * @param files
     * @param fileFlag
     * @return
     */
    public List<MultipartBody.Part> buildMultiParts(Map<String, String> params, List<File> files, String fileFlag) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (files != null && files.size() > 0) {
            for (File file : files) {
                parts.add(buildSigleMultipart(file, fileFlag));
            }
        }
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> p : params.entrySet()) {
                MultipartBody.Part part = MultipartBody.Part.createFormData(p.getKey(), p.getValue());
                parts.add(part);
            }
        }
        return parts;
    }

    /**
     * 构建单文件上传的 MultipartBody
     * APi 接口中不能使用@Multipart 注解
     *
     * @param file
     * @param fileFlag
     * @return
     */
    public MultipartBody buildSingleMultipartBody(File file, String fileFlag) {
        List<File> files = new ArrayList<>();
        files.add(file);
        return createFromData(null, files, fileFlag).build();
    }

    /**
     * 构建多文件上传的 MultipartBody
     * APi 接口中不能使用@Multipart 注解
     *
     * @param files
     * @param fileFlag
     * @return
     */
    public MultipartBody buildMultipartBodys(List<File> files, String fileFlag) {
        return createFromData(null, files, fileFlag).build();
    }

    /**
     * 构建文本参数和 多文件同时上传的 MultipartBody
     * APi 接口中不能使用@Multipart 注解
     *
     * @param params
     * @param files
     * @param fileFlag
     * @return
     */
    public MultipartBody buildMultipartBodys(Map<String, String> params, List<File> files, String fileFlag) {
        return createFromData(params, files, fileFlag).build();
    }

    /**
     * 构建一个 MultipartBody 所需的数据
     * APi 接口中不能使用@Multipart 注解
     *
     * @param files
     * @param fileFlag
     * @return
     */
    public MultipartBody.Builder createFromData(Map<String, String> params, List<File> files, String fileFlag) {
        //创建一个MultipartBody Builder对象，指定提交的类型为from表单形式
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        //添加普通参数
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> p : params.entrySet()) {
                builder.addFormDataPart(p.getKey(), p.getValue());
            }
        }
        //添加文件
        if (files != null && files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i));
                builder.addFormDataPart(fileFlag, files.get(i).getName(), body);
            }
        }
        return builder;
    }

}

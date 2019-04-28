package com.jzw.retrofit.base;

import java.io.Serializable;
import java.util.List;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by Administrator
 * @date 2019/4/26 0026
 * @change
 * @describe describe
 **/
public class Rsp<T> implements Serializable {
    private int code;
    private String message;
    private DataEntry<T> data;


    public List<T> getList() {
        if (getData() != null) {
            return getData().getList();
        }
        return null;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEntry<T> getData() {
        return data;
    }

    public void setData(DataEntry<T> data) {
        this.data = data;
    }
}

package com.jzw.retrofit.base;

import java.io.Serializable;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2018/9/14 0014
 * @change
 * @describe 实体类的基类
 **/
public class BaseEntry implements Serializable {
    static final long serialVersionUID = 42L;


    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseEntry{" +
                "id='" + id + '\'' +
                '}';
    }
}

package com.jzw.retrofit.base;

import java.util.List;

public class DataEntry<D> {
    private int total;
    private List<D> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<D> getList() {
        return list;
    }

    public void setList(List<D> list) {
        this.list = list;
    }
}
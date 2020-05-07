package com.jzw.retrofit.base;

import androidx.annotation.Keep;

import com.jzw.dev.http.entry.RspEntry;

import java.util.List;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2017/11/28 0028
 * @change
 * @describe 接口请求基类 data 为对象 { list:[]}
 **/
@Keep
public class ResultList<T> extends RspEntry {
    private ListData<T> data;

    public ListData getData() {
        return data;
    }

    public List<T> getList() {
        if (getData() != null) {
            return getData().getList();
        }
        return null;
    }

    public void setBaseData(ListData data) {
        this.data = data;
    }

    public class ListData<H> {
        private Integer total;
        private List<H> list;

        public List<H> getList() {
            return list;
        }

        public void setList(List<H> list) {
            this.list = list;
        }

        public Integer getTotal() {
            return total;
        }
    }
}

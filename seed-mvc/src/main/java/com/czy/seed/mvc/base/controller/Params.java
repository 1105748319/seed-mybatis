package com.czy.seed.mvc.base.controller;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by 003914[panlc] on 2017-06-02.
 */
public class Params implements Serializable {

    private int pageNum;
    private int pageSize;
    private Map<String, Object> params;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}

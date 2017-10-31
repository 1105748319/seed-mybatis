package com.czy.seed.mvc.sys.entity;

import com.czy.seed.mvc.base.entity.PrepareEntity;

/**
 * Created by 003914[panlc] on 2017-06-05.
 */
public class SysDict extends PrepareEntity  {

    private static final long serialVersionUID = -3036113094653530550L;
    private String code;
    private String name;
    private String memo;
    private Long parentId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

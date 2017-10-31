package com.czy.seed.mvc.sys.entity;

import com.czy.seed.mvc.base.entity.BaseEntity;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by PLC on 2017/5/23.
 */
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 4659702439449421452L;

    private String code;
    private String name;
    private String memo;

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

    @Override
    public String toString() {
        return "SysRole{" +
                super.toString() + '\'' +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}

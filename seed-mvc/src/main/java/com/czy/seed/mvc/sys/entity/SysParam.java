package com.czy.seed.mvc.sys.entity;

import com.czy.seed.mybatis.config.mybatis.annotations.ColumnType;
import org.apache.ibatis.type.JdbcType;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 系统参数配置
 * Created by PLC on 2017/6/3.
 */
public class SysParam implements Serializable {
    private static final long serialVersionUID = -2671018678667359634L;

    @Id
    private Long id;
    private String code;
    private String name;
    private String value;
    private Integer active;
    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

//    public boolean isActive() {
//        return active;
//    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getActive() {
        return active;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}

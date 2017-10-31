package com.czy.seed.mybatis.base.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by panlc on 2017-03-16.
 */
public class BaseEntity implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 数据创建时间
     */
    private Date createdDt;
    /**
     * 数据创建人
     */
    private Long createdBy;
    /**
     * 数据修改时间
     */
    private Date updatedDt;
    /**
     * 数据修改人
     */
    private Long updatedBy;
    /**
     * 乐观锁版本
     */
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedDt() {
        return updatedDt;
    }

    public void setUpdatedDt(Date updatedDt) {
        this.updatedDt = updatedDt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 是否自动生成createdDt/By、updatedDT/by、version字段
     * @return
     */
    public boolean autoConfig() {
        return true;
    }

}

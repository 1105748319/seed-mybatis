package com.czy.seed.mvc.base.entity;

import java.util.Date;

/**
 * Created by 003914[panlc] on 2017-06-13.
 */
public class PrepareEntity extends BaseEntity {
    /**
     * 数据创建时间
     */
    private Date createDt;

    /**
     * 数据创建人
     */
    private Long createBy;

    /**
     * 数据修改时间
     */
    private Date updateDt;

    /**
     * 数据修改人
     */
    private Long updateBy;

    /**
     * 逻辑删除
     */
    private Integer logicDel;

    /**
     * 乐观锁版本
     */
    private Integer version;

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getLogicDel() {
        return logicDel;
    }

    public void setLogicDel(Integer logicDel) {
        this.logicDel = logicDel;
    }
}

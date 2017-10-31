package com.czy.seed.mvc.base.entity;

import java.util.Date;

/**
 * 如果实体类实现了该接口，则会自动生成下列属性信息
 * Created by 003914[panlc] on 2017-06-13.
 */
public interface IBaseEntity{
    Date getCreateDt() ;

    void setCreateDt(Date createDt);

    Long getCreateBy() ;

    void setCreateBy(Long createBy) ;

    Date getUpdateDt();

    void setUpdateDt(Date updateDt) ;

    Long getUpdateBy();

    void setUpdateBy(Long updateBy) ;

    Integer getVersion();

    void setVersion(Integer version) ;

    Integer getLogicDel();

    void setLogicDel(Integer logicDel);

}

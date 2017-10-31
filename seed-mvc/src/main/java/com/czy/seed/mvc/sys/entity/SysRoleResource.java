package com.czy.seed.mvc.sys.entity;

import com.czy.seed.mybatis.config.mybatis.annotations.One2Many;
import com.czy.seed.mybatis.config.mybatis.annotations.One2One;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 角色资源
 * Created by PLC on 2017/5/23.
 */
public class SysRoleResource implements Serializable {
    private static final long serialVersionUID = -3755254892758648117L;
    @Id
    private Long id;
    private Long sysRoleId;
    private Long sysResourceId;

    @One2One(columns = "sys_resource_id=id")
    private SysResource sysResource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public Long getSysResourceId() {
        return sysResourceId;
    }

    public void setSysResourceId(Long sysResourceId) {
        this.sysResourceId = sysResourceId;
    }

    public SysResource getSysResource() {
        return sysResource;
    }

    public void setSysResource(SysResource sysResource) {
        this.sysResource = sysResource;
    }
}

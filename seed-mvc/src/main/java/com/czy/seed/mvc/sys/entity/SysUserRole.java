package com.czy.seed.mvc.sys.entity;

import com.czy.seed.mybatis.config.mybatis.annotations.One2One;

import java.io.Serializable;

/**
 * 用户、角色关联类
 * Created by panlc on 2017-05-23.
 */
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 4511028982475620265L;

    private Long id;
    private Long sysUserId;
    private Long sysRoleId;

    @One2One(columns = "sys_role_id = id")
    private SysRole sysRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Long getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public SysRole getSysRole() {
        return sysRole;
    }

    public void setSysRole(SysRole sysRole) {
        this.sysRole = sysRole;
    }
}

package com.czy.seed.mvc.sys.service;

import com.czy.seed.mvc.sys.entity.SysUserRole;
import com.czy.seed.mvc.base.service.BaseService;

import java.util.List;

/**
 * Created by PLC on 2017/5/23.
 */
public interface SysAccountRoleService extends BaseService<SysUserRole> {
    void saveUserRole(List<SysUserRole> userRoleList);
}

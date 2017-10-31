package com.czy.seed.mvc.sys.service;

import com.czy.seed.mvc.base.service.BaseService;
import com.czy.seed.mvc.sys.entity.SysRoleResource;

import java.util.List;

/**
 * Created by PLC on 2017/5/23.
 */
public interface SysRoleResourceService extends BaseService<SysRoleResource> {
    void saveRoleResources(List<SysRoleResource> roleResourceList);
}

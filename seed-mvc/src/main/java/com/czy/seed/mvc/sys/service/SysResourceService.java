package com.czy.seed.mvc.sys.service;

import com.czy.seed.mvc.base.service.BaseService;
import com.czy.seed.mvc.sys.entity.SysResource;

import java.util.List;

/**
 * Created by PLC on 2017/5/29.
 */
public interface SysResourceService extends BaseService<SysResource> {

    /**
     * 查找资源树
     * @return
     */
    List<SysResource> findResourceTreeForLoginUser();

    List<SysResource> selectResourceTree();
}

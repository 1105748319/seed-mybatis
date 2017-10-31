package com.czy.seed.mvc.sys.service.impl;

import com.czy.seed.mvc.base.service.impl.BaseServiceImpl;
import com.czy.seed.mvc.sys.entity.SysUserRole;
import com.czy.seed.mvc.sys.service.SysAccountRoleService;
import com.czy.seed.mybatis.base.QueryParams;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PLC on 2017/5/23.
 */
@Service
public class SysAccountRoleServiceImpl extends BaseServiceImpl<SysUserRole> implements SysAccountRoleService {


    /**
     * 添加用户角色
     * @param userRoleList
     */
    @Override
    public void saveUserRole(List<SysUserRole> userRoleList) {
        QueryParams queryParams = new QueryParams(SysUserRole.class);
        QueryParams.Criteria criteria = queryParams.createCriteria();
        criteria.andEqualTo("sysUserId", userRoleList.get(0).getSysUserId());
        super.deleteByParams(queryParams);
        super.insertList(userRoleList);
    }
}

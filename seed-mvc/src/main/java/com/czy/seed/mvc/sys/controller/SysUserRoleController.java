package com.czy.seed.mvc.sys.controller;

import com.czy.seed.mvc.sys.entity.SysUserRole;
import com.czy.seed.mvc.sys.service.SysAccountRoleService;
import com.czy.seed.mvc.util.Res;
import com.czy.seed.mybatis.base.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 003914[panlc] on 2017-06-06.
 */
@RestController
@RequestMapping("/sys/userRole")
public class SysUserRoleController {

    @Autowired
    private SysAccountRoleService sysAccountRoleService;


    @RequestMapping("/selectList")
    public Res selectList() {
        QueryParams queryParams = new QueryParams(SysUserRole.class);
        List<SysUserRole> sysUserRoles = sysAccountRoleService.selectListByParams(queryParams);
        return Res.ok(sysUserRoles);
    }

    @RequestMapping("/selectListForUser/{userId}")
    public Res selectListForUser(@PathVariable long userId) {
        QueryParams queryParams = new QueryParams(SysUserRole.class);
        QueryParams.Criteria criteria = queryParams.createCriteria();
        criteria.andEqualTo("sysUserId", userId);
        List<SysUserRole> sysUserRoles = sysAccountRoleService.selectListRelativeByParams(queryParams);
        return Res.ok(sysUserRoles);
    }

    /**
     * 保存用户权限
     *
     * @param userRoleList 用户角色列表
     * @return Res
     */
    @RequestMapping("/saveUserRole")
    public Res saveUserRole(@RequestBody List<SysUserRole> userRoleList) {
        sysAccountRoleService.saveUserRole(userRoleList);
        return Res.ok();
    }


}

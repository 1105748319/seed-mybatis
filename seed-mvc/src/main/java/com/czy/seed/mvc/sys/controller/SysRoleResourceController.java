package com.czy.seed.mvc.sys.controller;

import com.czy.seed.mvc.sys.entity.SysOrg;
import com.czy.seed.mvc.sys.entity.SysRoleResource;
import com.czy.seed.mvc.sys.service.SysRoleResourceService;
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
@RequestMapping("/sys/roleResource")
public class SysRoleResourceController {

    @Autowired
    private SysRoleResourceService sysRoleResourceService;

    /**
     * 查询角色对应菜单
     * @param roleId
     * @return
     */
    @RequestMapping("/selectResourceForRole/{roleId}")
    public Res selectResourceForRole(@PathVariable long roleId) {
        QueryParams queryParams = new QueryParams(SysRoleResource.class);
        QueryParams.Criteria criteria = queryParams.createCriteria();
        criteria.andEqualTo("sysRoleId", roleId);
        List<SysRoleResource> list = sysRoleResourceService.selectListRelativeByParams(queryParams);
        return Res.ok(list);
    }

    @RequestMapping("/save")
    public Res save(@RequestBody SysRoleResource sysRoleResource) {
        if (sysRoleResource.getId() == null) {
            sysRoleResourceService.insert(sysRoleResource);
        } else {
            sysRoleResourceService.updateByPrimaryKeySelective(sysRoleResource);
        }
        return Res.ok(sysRoleResource.getId());
    }

    @RequestMapping("/deleteByPrimary/{id}")
    public Res deleteByPrimary(@PathVariable long id) {
        sysRoleResourceService.deleteByPrimaryKey(id);
        return Res.ok();
    }

    /**
     * insertList
     *
     * @param roleResourceList
     * @return
     */
    @RequestMapping("/saveRoleResource")
    public Res saveRoleResource(@RequestBody List<SysRoleResource> roleResourceList) {
        sysRoleResourceService.saveRoleResources(roleResourceList);
        return Res.ok();
    }

}

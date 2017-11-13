package com.czy.seed.mvc.sys.controller;

import com.czy.seed.mvc.sys.entity.SysResource;
import com.czy.seed.mvc.util.PrincipalUtil;
import com.czy.seed.mvc.util.Res;
import com.czy.seed.mvc.sys.service.SysResourceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by PLC on 2017/5/29.
 */
@RestController
@RequestMapping("/sys/resource")
public class SysResourceController {

    @Autowired
    private SysResourceService sysResourceService;

    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("/sys/resource");
    }

    /**
     * 查找登陆用户的资源树
     * @return
     */
    @RequestMapping("/findResourceTreeForLoginUser")
    public List<SysResource> findResourceTreeForLoginUser() {
        List<SysResource> sysResourceList  = null;
        if(! "ROLE_ANONYMOUS".equals(StringUtils.join(PrincipalUtil.getLoginUserRoles(),","))){
            return sysResourceList = sysResourceService.findResourceTreeForLoginUser();
        }

        return sysResourceList;
    }

    /**
     * 查找所有资源树
     * @return
     */
    @RequestMapping("/selectResourceTree")
    public Res selectResourceTree() {
        List<SysResource> sysResources = sysResourceService.selectResourceTree();
        return Res.ok(sysResources);
    }

    /**
     * 保存组织机构
     * @param sysResource
     * @return 操作成功的数据id值
     */
    @RequestMapping("/save")
    public Res save(@RequestBody SysResource sysResource) {
        if (sysResource.getId() == null) {
            sysResourceService.insert(sysResource);
        } else {
            sysResourceService.updateByPrimaryKeySelective(sysResource);
        }
        return Res.ok(sysResource.getId().toString());
    }

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteByPrimary/{id}")
    public Res deleteByPrimary(@PathVariable long id) {
        sysResourceService.deleteByPrimaryKey(id);
        return Res.ok();
    }
}

package com.czy.seed.mvc.sys.controller;

import com.czy.seed.mvc.base.controller.BaseController;
import com.czy.seed.mvc.sys.entity.SysRole;
import com.czy.seed.mvc.sys.service.SysRoleService;
import com.czy.seed.mvc.util.Res;
import com.czy.seed.mybatis.base.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by 003914[panlc] on 2017-06-06.
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController<SysRole> {

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("/sys/role");
    }

    @RequestMapping("/deleteByPrimary/{id}")
    public Res deleteByPrimary(@PathVariable long id) {
        sysRoleService.deleteByPrimaryKey(id);
        return Res.ok();
    }

    @RequestMapping("/selectList")
    public Res selectList() {
        QueryParams queryParams = new QueryParams(SysRole.class);
        List<SysRole> sysRoles = sysRoleService.selectListByParams(queryParams);
        return Res.ok(sysRoles);
    }


}

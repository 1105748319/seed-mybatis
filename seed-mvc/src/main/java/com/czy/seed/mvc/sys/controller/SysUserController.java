package com.czy.seed.mvc.sys.controller;

import com.czy.seed.mvc.base.controller.Params;
import com.czy.seed.mvc.sys.entity.SysOrg;
import com.czy.seed.mvc.sys.entity.SysUser;
import com.czy.seed.mvc.sys.service.SysUserService;
import com.czy.seed.mvc.util.Res;
import com.czy.seed.mybatis.base.QueryParams;
import com.czy.seed.mybatis.tool.NullUtil;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 003914[panlc] on 2017-06-01.
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping("/selectByPage")
    public Res selectByPage(@RequestBody Params params) {
        QueryParams queryParams = new QueryParams(SysUser.class);
        Map<String, Object> paramsForQuery = params.getParams();
        if (paramsForQuery != null) {
            if (paramsForQuery.containsKey("name")) {
                Object name = paramsForQuery.get("name");
                if (NullUtil.isNotEmpty(name)) {
                    QueryParams.Criteria criteria = queryParams.createCriteria();
                    criteria.andLike("name", "%" + name + "%");
                }
            }
        }
        Page<SysUser> page = sysUserService.selectPageByParams(params.getPageNum(), params.getPageSize(), queryParams);
        Map<String, Object> pageInfo = new HashMap<String, Object>();
        pageInfo.put("total", page.getTotal());
        pageInfo.put("pages", page.getPages());
        pageInfo.put("page", page);
        return Res.ok(pageInfo);
    }

    /**
     * 保存用户信息
     *
     * @param sysUser
     * @return 操作成功的数据id值
     */
    @RequestMapping("/save")
    public String save(@RequestBody SysUser sysUser) {
        if (sysUser.getId() == null) {
            sysUserService.insert(sysUser);
        } else {
            sysUserService.updateByPrimaryKeySelective(sysUser);
        }
        return sysUser.getId().toString();
    }

    @RequestMapping("/deleteByPrimary/{id}")
    public Res deleteByPrimary(@PathVariable("id") long id) {
        sysUserService.deleteByPrimaryKey(id);
        return Res.ok();
    }

}

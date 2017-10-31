package com.czy.seed.mvc.sys.controller;

import com.czy.seed.mvc.sys.entity.SysOrg;
import com.czy.seed.mvc.sys.service.SysOrgService;
import com.czy.seed.mvc.util.Res;
import com.czy.seed.mybatis.base.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 组织机构管理
 * Created by PLC on 2017/5/30.
 */
@RestController
@RequestMapping("/sys/org")
public class SysOrgController {

    @Autowired
    private SysOrgService sysOrgService;

    /**
     * 功能首页
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("/sys/org");
        return mv;
    }

    /**
     * 查找组织机构树
     *
     * @return
     */
    @RequestMapping("/selectOrgTree")
    public List<SysOrg> selectOrgTree() {
        return sysOrgService.selectOrgTree();
    }

    /**
     * 保存组织机构
     * @param sysOrg
     * @return 操作成功的数据id值
     */
    @RequestMapping("/save")
    public String save(@RequestBody SysOrg sysOrg) {
        if (sysOrg.getId() == null) {
            sysOrgService.insert(sysOrg);
        } else {
            sysOrgService.updateByPrimaryKeySelective(sysOrg);
        }
        return sysOrg.getId().toString();
    }

    @RequestMapping("/loadData")
    public SysOrg loadData(long id) {
        return sysOrgService.selectByPrimaryKey(id);
    }

    @RequestMapping("/deleteOrg")
    public Res deleteOrg(long id) {
        QueryParams params = new QueryParams(SysOrg.class);
        QueryParams.Criteria criteria = params.createCriteria();
        criteria.andEqualTo("parentId", id);
        int count = sysOrgService.selectCountByParams(params);
        if (count > 0) {
            return Res.error("当前机构有下属机构，无法删除");
        }
        sysOrgService.deleteByPrimaryKey(id);
        return Res.ok();
    }

}


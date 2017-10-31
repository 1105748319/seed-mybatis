package com.czy.seed.mvc.sys.service.impl;

import com.czy.seed.mvc.base.service.impl.BaseServiceImpl;
import com.czy.seed.mvc.sys.entity.SysOrg;
import com.czy.seed.mvc.sys.entity.SysResource;
import com.czy.seed.mvc.sys.mapper.SysOrgMapper;
import com.czy.seed.mvc.sys.service.SysOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by PLC on 2017/5/30.
 */
@Service
public class SysOrgServiceImpl extends BaseServiceImpl<SysOrg> implements SysOrgService {

    @Autowired
    private SysOrgMapper sysOrgMapper;

    /**
     * 查找组织结构树
     * @return
     */
    @Override
    public List<SysOrg> selectOrgTree() {
        List<SysOrg> sysOrgs = sysOrgMapper.selectAllOrgs();
        return buildTree(sysOrgs);
    }

    /**
     * 构造组织树
     * @param sysOrgs
     * @return
     */
    public List<SysOrg> buildTree(List<SysOrg> sysOrgs) {
        List<SysOrg> rootOrg = findRootOrg(sysOrgs);
        findChildrenResource(sysOrgs, rootOrg);
        return rootOrg;
    }

    /**
     * 查找根节点
     * @param orgList
     * @return
     */
    private List<SysOrg> findRootOrg(List<SysOrg> orgList) {
        List<SysOrg> rootOrgList = new ArrayList<SysOrg>();
        for (SysOrg org : orgList) {
            if (0 == org.getParentId()) {
                rootOrgList.add(org);
            }
        }
        orgList.remove(rootOrgList);
        return rootOrgList;
    }

    /**
     * 设置父节点的所有子节点——递归调用
     * @param orgWithoutRoot 不包含父节点的所有组织列表
     * @param rootList 根节点
     */
    public void findChildrenResource(List<SysOrg> orgWithoutRoot, List<SysOrg> rootList) {
        List<SysOrg> subList = new ArrayList<SysOrg>();  //本轮未查找到归属的节点集合
        Iterator<SysOrg> iterator = orgWithoutRoot.iterator();
        List<SysOrg> children = new ArrayList<SysOrg>();  //查找到归属的节点集合
        while (iterator.hasNext()) {
            SysOrg resource = iterator.next();
            boolean flag = false;   //当为真时，表示当前iterator已经被识别为子节点
            for (SysOrg parent : rootList) {
                if (resource.getParentId() == parent.getId()) {
                    parent.getChildren().add(resource);
                    children.add(resource);
                    flag = true;
                }
            }
            if(!flag) {
                subList.add(resource);
            }
        }
        if (subList.size() > 0 && children.size() > 0) {
            findChildrenResource(subList, children);
        }
    }

}

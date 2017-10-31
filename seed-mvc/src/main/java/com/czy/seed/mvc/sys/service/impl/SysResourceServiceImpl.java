package com.czy.seed.mvc.sys.service.impl;

import com.czy.seed.mvc.auth.UserAuthority;
import com.czy.seed.mvc.base.service.impl.BaseServiceImpl;
import com.czy.seed.mvc.sys.entity.SysResource;
import com.czy.seed.mvc.sys.mapper.SysResourceMapper;
import com.czy.seed.mvc.sys.service.SysResourceService;
import com.czy.seed.mvc.util.PrincipalUtil;
import com.czy.seed.mybatis.base.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by PLC on 2017/5/29.
 */
@Service
public class SysResourceServiceImpl extends BaseServiceImpl<SysResource> implements SysResourceService {

    @Autowired
    private SysResourceMapper sysResourceMapper;

    /**
     * 查找用户所有权限
     *
     * @return 登陆用户菜单列表
     */
    public List<SysResource> findResourceTreeForLoginUser() {
        //获取登陆用户角色
        List<UserAuthority> loginUserRoles = PrincipalUtil.getLoginUserRoles();
        String roleIds = getRoleIds(loginUserRoles);

        List<SysResource> resources = sysResourceMapper.findResourceForLoginUser(roleIds);   //查询用户所有菜单
        //关联查询所有菜单的父级菜单
        List<SysResource> fullResources = new ArrayList<SysResource>();
        List<SysResource> fullResourceList = findFullResources(resources, resources);

        return buildTree(fullResourceList);
    }

    /**
     * 获取用户角色主键信息
     *
     * @param loginUserRoles 登陆用户角色
     * @return 登陆用户角色id字符串
     */
    private String getRoleIds(List<UserAuthority> loginUserRoles) {
        //封装角色id
        StringBuffer sb = new StringBuffer("(");
        for (UserAuthority userAuthority : loginUserRoles) {
            sb.append(userAuthority.getRoleId()).append(",");
        }
        String substring = sb.substring(0, sb.length() - 1);
        substring = substring +  ")";
        return substring;
    }

    /**
     * 递归查询用户完善权限树
     *
     * @param resources 当前菜单项
     * @param fullResources 所有菜单列表
     * @return 所有菜单列表
     */
    private List<SysResource> findFullResources(List<SysResource> resources, List<SysResource> fullResources) {
        if (resources == null || resources.size() == 0) {
            return fullResources;
        }

        //生成父级资源id列表
        Set<Long> ids = new HashSet<Long>(resources.size());
        List<SysResource> parentResources = new ArrayList<SysResource>();
        for (SysResource resource : resources) {
            Long parentId = resource.getParentId();
            if (parentId != 0) {
                ids.add(parentId);
            }
        }

        //查找父级资源
        if (ids.size() > 0) {
            QueryParams queryParams = new QueryParams(SysResource.class);
            QueryParams.Criteria criteria = queryParams.createCriteria();
            criteria.andIn("id", ids);
            parentResources = sysResourceMapper.selectListByParams(queryParams);   //查找父级资源
            for (SysResource par : parentResources) {
                boolean have = false;
                for (SysResource full : fullResources) {
                    if (par.getId() == full.getId()) {
                        have = true;
                        break;
                    }
                }
                if (!have) {
                    fullResources.add(par);
                }
            }
        }
        return findFullResources(parentResources, fullResources);  //递归查找;
    }

    /**
     * 查询所有资源树
     * @return
     */
    public List<SysResource> selectResourceTree() {
        List<SysResource> allResources = sysResourceMapper.selectListAll();
        return buildTree(allResources);
    }

    /**
     * 构造资源树
     *
     * @param allResources
     * @return
     */
    public List<SysResource> buildTree(List<SysResource> allResources) {
        List<SysResource> rootResource = findRootResource(allResources);
        findChildrenResource(allResources, rootResource);
        //构造虚拟根节点：一个id为0的节点，永远展示在页面上
        SysResource zeroNode = new SysResource();
        zeroNode.setName("系统菜单");
        zeroNode.setId(0L);
        zeroNode.setParentId(-1L);
        zeroNode.getChildren().addAll(rootResource);
        List<SysResource> resources = new ArrayList<SysResource>();
        resources.add(zeroNode);

        //排序
        orderBy(resources);
        return resources;
    }

    private void orderBy(List<SysResource> resources) {
        resources.sort(new Comparator<SysResource>() {
            @Override
            public int compare(SysResource o1, SysResource o2) {
                int orderByDiff = o1.getOrderBy() - o2.getOrderBy();
                if (orderByDiff != 0) {
                    return orderByDiff;
                } else {
                    String idDiff = o1.getId() - o2.getId() + "";
                    return Integer.parseInt(idDiff);
                }
            }
        });
        for (SysResource resource : resources) {
            List<SysResource> children = resource.getChildren();
            if (children != null && children.size() > 0) {
                orderBy(children);
            }
        }
    }

    /**
     * 查找根节点
     *
     * @param allResources
     * @return
     */
    private List<SysResource> findRootResource(List<SysResource> allResources) {
        List<SysResource> rootResource = new ArrayList<SysResource>();
        for (SysResource resource : allResources) {
            if (0 == resource.getParentId()) {
                rootResource.add(resource);
            }
        }
        allResources.removeAll(rootResource);
        return rootResource;
    }

    /**
     * 设置父节点的所有子节点——递归调用
     *
     * @param allResourcesWithoutRoot allResourcsWithoutRoot不包含父节点的所有资源列表
     * @param rootResourceList        根节点
     */
    public void findChildrenResource(List<SysResource> allResourcesWithoutRoot, List<SysResource> rootResourceList) {
        List<SysResource> subResources = new ArrayList<SysResource>();  //本轮未查找到归属的节点集合
        Iterator<SysResource> iterator = allResourcesWithoutRoot.iterator();
        List<SysResource> children = new ArrayList<SysResource>();  //查找到归属的节点集合
        while (iterator.hasNext()) {
            SysResource resource = iterator.next();
            boolean flag = false;   //当为真时，表示当前iterator已经被识别为子节点
            for (SysResource parentResource : rootResourceList) {
                if (resource.getParentId() == parentResource.getId()) {
                    parentResource.getChildren().add(resource);
                    children.add(resource);
                    flag = true;
                }
            }
            if (!flag) {
                subResources.add(resource);
            }
        }
        if (subResources.size() > 0 && children.size() > 0) {
            findChildrenResource(subResources, children);
        }
    }


}

package com.czy.seed.mvc.sys.mapper;

import com.czy.seed.mvc.sys.entity.SysResource;
import com.czy.seed.mybatis.base.mapper.BaseMapper;
import com.czy.seed.mybatis.config.mybatis.annotations.AutoMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by PLC on 2017/5/29.
 */
@AutoMapper
public interface SysResourceMapper extends BaseMapper<SysResource> {

    /**
     * 查询登陆用户可用资源
     *
     * @param roleIds 登陆用户角色ids
     * @return 可用资源列表
     */
    @Select("select sr.*, sr.parent_id as parentId, sr.order_by as orderBy from sys_resource sr where EXISTS " +
            "(select srr.id from sys_role_resource srr where sr.id = srr.SYS_RESOURCE_ID and srr.SYS_ROLE_ID in ${userId})")
    List<SysResource> findResourceForLoginUser(@Param("userId") String roleIds);

    /**
     * 查找所有资源列表
     * @return 所有资源列表
     */
    @Select("select id,parent_id as parentId, types, code, name, url, order_by as orderBy, icon " +
            "from sys_resource")
    List<SysResource> selectListAll();

}

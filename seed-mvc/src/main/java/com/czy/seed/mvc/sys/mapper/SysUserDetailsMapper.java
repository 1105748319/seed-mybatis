package com.czy.seed.mvc.sys.mapper;

import com.czy.seed.mvc.auth.SecurityUser;
import com.czy.seed.mvc.sys.entity.SysUser;
import com.czy.seed.mybatis.base.mapper.BaseMapper;
import com.czy.seed.mybatis.config.mybatis.annotations.AutoMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Comup on 2017/5/23.
 */
@AutoMapper
public interface SysUserDetailsMapper extends BaseMapper<SecurityUser> {

    @Select("select * from sys_user where username=#{username}")
    SysUser selectByUsername(@Param("username") String username);

}

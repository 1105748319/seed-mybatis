package com.czy.seed.mvc.auth;

import com.czy.seed.mvc.sys.entity.SysRole;
import com.czy.seed.mvc.sys.entity.SysUser;
import com.czy.seed.mvc.sys.mapper.SysUserDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Comup on 2017/5/23.
 * 查询用户服务类
 */
@Service
public class SysUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserDetailsMapper sysUserDetailsMapper;

    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.trim().equals("")) {
            throw new UsernameNotFoundException("username maybe is null or empty");
        }
        SysUser sysUser = sysUserDetailsMapper.selectByUsername(username);
        SecurityUser securityUser = new SecurityUser(sysUser);
        if (securityUser == null) {
            throw new UsernameNotFoundException("User:" + username + "not found");
        }
        return securityUser;
    }
}
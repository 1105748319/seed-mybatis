package com.czy.seed.mvc.sys.entity;

import com.czy.seed.mybatis.config.mybatis.annotations.Cache;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * Created by PLC on 2017/5/23.
 */
//@Cache
public class SysUser implements Serializable {

    private static final long serialVersionUID = 6022588492678547207L;

    @Id
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean enable;                 //账号是否可用
    private boolean nonLocked;       //账号是否锁定
    private Date credentialsExpiredTime; //密码过期时间
    private Date accountExpiredTime;     //账号过期时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isNonLocked() {
        return nonLocked;
    }

    public void setNonLocked(boolean nonLocked) {
        this.nonLocked = nonLocked;
    }

    public Date getCredentialsExpiredTime() {
        return credentialsExpiredTime;
    }

    public void setCredentialsExpiredTime(Date credentialsExpiredTime) {
        this.credentialsExpiredTime = credentialsExpiredTime;
    }

    public Date getAccountExpiredTime() {
        return accountExpiredTime;
    }

    public void setAccountExpiredTime(Date accountExpiredTime) {
        this.accountExpiredTime = accountExpiredTime;
    }
}

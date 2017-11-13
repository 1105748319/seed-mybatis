package com.czy.seed.mvc.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Created by PLC on 2017/5/29.
 */
public class UserAuthority implements GrantedAuthority, Serializable {

    private final String role;
    private final Long roleId;

    public UserAuthority(String role, Long roleId) {
        Assert.hasText(role, "A granted authority textual representation is required");
        Assert.notNull(roleId, "A granted authority roleId representation is required");
        this.role = role;
        this.roleId = roleId;
    }


    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public int hashCode() {
        return 31 ^ roleId.hashCode() ^ role.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UserAuthority) {
            UserAuthority ua = (UserAuthority) obj;
            return this.role.equals(ua.role) && this.roleId.equals(ua.roleId);
        }
        return false;
    }

    @Override
    public String toString() {
        return "UserAuthority{" +
                "role='" + role + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}

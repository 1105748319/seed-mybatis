package com.czy.seed.mvc.sys.entity;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PLC on 2017/5/30.
 */
public class SysOrg implements Serializable {
    private static final long serialVersionUID = 1172246239374961171L;

    @Id
    private Long id;
    private Long parentId;
    private String code;
    private String name;
    private String memo;

    @Transient
    private List<SysOrg> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<SysOrg> getChildren() {
        if (children == null) {
            children = new ArrayList<SysOrg>();
        }
        return children;
    }

}

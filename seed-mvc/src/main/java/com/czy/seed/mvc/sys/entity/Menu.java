package com.czy.seed.mvc.sys.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PLC on 2017/5/29.
 */
public class Menu {

    private List<Menu> children;

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}

package com.czy.seed.mvc.base.entity;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 003914[panlc] on 2017-06-08.
 */
public class BaseEntity implements Serializable {

    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id=" + id;
    }
}

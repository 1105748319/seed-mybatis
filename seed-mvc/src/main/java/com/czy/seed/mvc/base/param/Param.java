package com.czy.seed.mvc.base.param;

import com.czy.seed.mybatis.base.QueryParams;

import java.util.List;

/**
 * Created by PLC on 2017/6/10.
 */
public class Param {

    private List<Group> or;

    public List<Group> getOr() {
        return or;
    }

    public void setOr(List<Group> or) {
        this.or = or;
    }

    public QueryParams toQueryParams(Class clazz) {
        QueryParams queryParams = new QueryParams(clazz);
        if (this.getOr() != null) {
            for (Group or : this.getOr()) {
                or.appendParamGroup(queryParams);
            }
        }
        return queryParams;
    }
}

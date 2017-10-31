package com.czy.seed.mybatis.tool;

/**
 * Created by panlc on 2017-03-23.
 */
public class NullUtil {

    private NullUtil() {
    }

    public static final boolean isNull(Object obj) {
        return null == obj;
    }

    public static final boolean isNotNull(Object obj) {
        return null != obj;
    }

    public static final boolean isEmpty(Object obj) {
        return null == obj || "".equals(obj);
    }

    public static final boolean isNotEmpty(Object obj) {
        return null != obj && !"".equals(obj);
    }

}

package com.czy.seed.mybatis.sql.entity;

import com.czy.seed.mybatis.config.mybatis.annotations.Join;

/**
 * Created by panlc on 2017-03-30.
 */
public class MybatisConnection extends MybatisAssociation {

    public MybatisConnection(String mainTableName, String property, Class<?> targetClass, String fields, String columns, Join join) {
        super(mainTableName, property, targetClass, fields, columns, join);
    }

}

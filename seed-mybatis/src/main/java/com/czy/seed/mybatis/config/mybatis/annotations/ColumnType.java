package com.czy.seed.mybatis.config.mybatis.annotations;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 针对列的复杂属性配置
 *
 * Created by panlc on 2017-03-17.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnType {
    String column() default "";

    JdbcType jdbcType() default JdbcType.UNDEFINED;

    Class<? extends TypeHandler<?>> typeHandler() default UnknownTypeHandler.class;
}

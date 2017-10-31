package com.czy.seed.mybatis.config.mybatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一对一注解
 * Created by panlc on 2017-03-28.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface One2One {

    /**
     * 关联查询mapper
     * @return
     */
    String fields() default "";

    /**
     * 关联条件
     * @return
     */
    String columns();

    Join join() default Join.LEFT;

}

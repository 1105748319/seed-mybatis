package com.czy.seed.mybatis.config.mybatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by panlc on 2017-03-30.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface One2Many {

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

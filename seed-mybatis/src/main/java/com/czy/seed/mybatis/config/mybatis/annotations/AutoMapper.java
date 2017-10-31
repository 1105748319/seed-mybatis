package com.czy.seed.mybatis.config.mybatis.annotations;

/**
 * Created by panlc on 2017-03-17.
 */

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoMapper {

    String value() default "default";

}

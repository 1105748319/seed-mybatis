package com.czy.seed.mvc.annotation;

import java.lang.annotation.*;

/**
 * 自动记录系统日志注解
 * Created by 003914[panlc] on 2017-06-09.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {

    String value() default "";

}

package com.czy.seed.mybatis.config.mybatis.annotations;

import java.lang.annotation.*;

/**
 * Created by panlc on 2017-04-13.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    CacheType cacheType() default CacheType.MYBATIS;

    public enum CacheType{
        MYBATIS
    }

}

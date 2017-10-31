package com.czy.seed.mybatis.config;

import com.czy.seed.mybatis.tool.SpringPropertiesUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Created by panlc on 2017-05-23.
 */
@Component
public class SpringPropertiesUtilConfig {

    @Bean
    public SpringPropertiesUtil springPropertiesUtil() {
        SpringPropertiesUtil springPropertiesUtil = new SpringPropertiesUtil();
        springPropertiesUtil.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");
        springPropertiesUtil.setIgnoreResourceNotFound(true);
        springPropertiesUtil.setLocations(new ClassPathResource("classpath:/config/application.properties"));
        return springPropertiesUtil;
    }

}

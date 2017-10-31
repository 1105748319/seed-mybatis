package com.czy.seed.mybatis.config;

import com.czy.seed.mybatis.config.datasource.AtomikosDataSourceBuilder;
import com.czy.seed.mybatis.config.datasource.DataSourceBuilder;
import com.czy.seed.mybatis.config.mybatis.MybatisAtomikosConfig;
import com.czy.seed.mybatis.config.mybatis.MybatisConfig;
import com.czy.seed.mybatis.tool.SpringContextHelper;
import com.czy.seed.mybatis.tool.SpringPropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by PLC on 2017/5/3.
 */
@Component
public class DataBaseEnvInit {

    @Autowired
    private SpringContextHelper springContextHelper;

    /**
     * 根据配置的数据源类型，初始化不同的数据源和mybatis环境
     */
    @PostConstruct
    public void initDataSource() {
        boolean initNormalEvnFlag = true;   //是否初始化普通数据源

        if (SpringPropertiesUtil.containsKey("datasource.pool")) {
            String property = SpringPropertiesUtil.getProperty("datasource.pool");
            if (property.startsWith("atomikos")) {
                initNormalEvnFlag = false;
            }
        }
        //如果配置了动态数据源，则自动使用atomikos数据连接
        Map<String, Object> ctxPropertiesMap = SpringPropertiesUtil.getCtxPropertiesMap();
        for (String key : ctxPropertiesMap.keySet()) {
            if (key.startsWith(DataSourceBuilder.DYNAMIC_DATASOURCE_PREFIX)) {
                initNormalEvnFlag = false;
                break;
            }
        }

        if (initNormalEvnFlag) {
            initNormalEnv();
        } else {
            initAtomikosEnv();
        }
    }

    /**
     * 初始化atomikos数据源
     */
    private void initAtomikosEnv() {
        springContextHelper.addBean(AtomikosDataSourceBuilder.class,
                "dataSourceBuilder", null,
                "calPoolType", null);
        //初始化Mybatis
        springContextHelper.addBean(MybatisAtomikosConfig.class,
                "mybatisConfig", null,
                "registerTransactionManager", null);
    }

    /**
     * 初始化普通数据源
     */
    private void initNormalEnv() {
        springContextHelper.addBean(DataSourceBuilder.class,
                "dataSourceBuilder", null,
                "calPoolType", null);
        //初始化Mybatis
        springContextHelper.addBean(MybatisConfig.class,
                "mybatisConfig", null,
                "registerTransactionManager", null);
    }

}

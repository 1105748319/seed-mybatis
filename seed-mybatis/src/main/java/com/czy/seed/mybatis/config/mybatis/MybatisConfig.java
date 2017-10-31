package com.czy.seed.mybatis.config.mybatis;

import com.czy.seed.mybatis.config.datasource.DataSourceBuilder;
import com.czy.seed.mybatis.config.exception.ConfigErrorException;
import com.czy.seed.mybatis.tool.NullUtil;
import com.czy.seed.mybatis.tool.SpringContextHelper;
import com.czy.seed.mybatis.tool.SpringPropertiesUtil;
import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;

/**
 * Created by panlc on 2017-02-16.
 */
public class MybatisConfig {

    private static Map<String, String> sqlSessionDialect = new HashMap<String, String>();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SpringContextHelper springContextHelper;

    private DataSourceBuilder dataSourceBuilder;

    @Autowired
    public MybatisConfig(SpringContextHelper springContextHelper, DataSourceBuilder dataSourceBuilder) {
        this.springContextHelper = springContextHelper;
        this.dataSourceBuilder = dataSourceBuilder;
    }

    public String getSqlSessionDialect(String sqlSessionName) {
        if (!sqlSessionDialect.containsKey(sqlSessionName)) {
            throw new ConfigErrorException("id为" + sqlSessionName + "的bean未初始化");
        }
        return sqlSessionDialect.get(sqlSessionName);
    }

    @PostConstruct
    public void registerTransactionManager() {
        registerDynamicSqlSessionFactory();
        registerDefaultTransactionManager();
        registerDynamicTransactionManager();
    }

    /**
     * 注册默认据源Transactionmanager
     */
    public void registerDefaultTransactionManager() {
        registerTransactionmanager(DataSourceBuilder.DATASOURCE_BEAN_PREFIX
                + DataSourceBuilder.MAIN_DATASOURCE_SUFFIX, "transactionManager");
    }

    /**
     * 注册各动态数据源Transactionmanager
     */
    public void registerDynamicTransactionManager() {
        Map<String, String> customDataSources = dataSourceBuilder.getDatasourceDialect();
        for (String dataSourceBeanName : customDataSources.keySet()) {
            String tranBeanName = "tm-" + getDatasourceName(dataSourceBeanName);
            registerTransactionmanager(dataSourceBeanName, tranBeanName);
        }
    }



    /**
     * 为数据源创建Transactionmanager
     *
     * @param dataSourceName 数据源名称
     */
    private void registerTransactionmanager(String dataSourceName, String tranBeanName) {
        try {
            DataSource dataSource = springContextHelper.getBeanById(dataSourceName);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("dataSource", dataSource);
            springContextHelper.addBean(DataSourceTransactionManager.class, tranBeanName, map, null, null);
        } catch (Exception e) {
            logger.warn("error occurred while register {} to spring", tranBeanName, e);
        }
    }

    /**
     * 动态注册数据源Transactionmanager
     */
    private void registerDynamicSqlSessionFactory() {
        Map<String, String> datasourceDialect = dataSourceBuilder.getDatasourceDialect();
        for (String key : datasourceDialect.keySet()) {
            registerSqlSessionFactory(key);
        }
    }

    /**
     * 为不同数据源创建SqlSessionFactory
     *
     * @param dataSourceName
     */
    private void registerSqlSessionFactory(String dataSourceName) {
        String sqlSessionFactoryName = "sqlSessionFactory-" + getDatasourceName(dataSourceName);
        try {
            DataSource dataSource = springContextHelper.getBeanById(dataSourceName);
            String dialect = dataSourceBuilder.getDialect(dataSourceName);
            Map<String, Object> resource = new HashMap<String, Object>();
            resource.put("dataSource", dataSource);
//            resource.put("plugins", new ProcedureSqlInt());       //TODO 添加插件

            PageInterceptor interceptor = new PageInterceptor();
            Map<String, String> pagehelper = new LinkedHashMap<String, String>();
            Properties properties = new Properties();
            properties.putAll(pagehelper);
            interceptor.setProperties(properties);
            resource.put("plugins", interceptor);

            //加载mybatis config文件
            String configLocation = SpringPropertiesUtil.getStringProperty("mybatis.configurationLocations");
            if (NullUtil.isEmpty(configLocation)) {
                configLocation = "classpath:core/mybatis-config.xml";
            }
            resource.put("configLocation", configLocation);

            //加载mapper文件
            String mapperLocations = SpringPropertiesUtil.getStringProperty("mybatis.mapperLocations");
            if (NullUtil.isNotEmpty(mapperLocations)) {
                String[] mapperLocationsConfig = mapperLocations.split(",");
                resource.put("mapperLocations", Arrays.asList(mapperLocationsConfig));
            }

            //TODO 增加插件

            springContextHelper.addBean(SqlSessionFactoryBean.class, sqlSessionFactoryName, resource, null, null);
            sqlSessionDialect.put(sqlSessionFactoryName, dialect); //TODO注册sqlSessionFactory类型



        } catch (Exception e) {
            logger.error("error occurred while register {} to spring", sqlSessionFactoryName, e);
            throw new RuntimeException("error occurred while register " + sqlSessionFactoryName + " to spring", e);
        }
    }


    public Resource[] resolveMapperLocations(String... mapperLocationsConfig) {
        List<Resource> resources = new ArrayList<Resource>();
        if (mapperLocationsConfig != null) {
            for (String mapperLocation : mapperLocationsConfig) {
                Resource[] mappers;
                try {
                    mappers = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                } catch (IOException e) {
                    logger.error("error occurred: ", e);
                }
            }
        }

        Resource[] mapperLocations = new Resource[resources.size()];
        mapperLocations = resources.toArray(mapperLocations);
        return mapperLocations;
    }

    /**
     * 取数据源名称
     *
     * @param beanName 数据源id
     * @return 数据源配置名称
     */
    private String getDatasourceName(String beanName) {
        if (beanName == null || "".equals(beanName)) {
            throw new RuntimeException("beanName can't be null or empty");
        }
        int i = beanName.indexOf("-");
        return beanName.substring(i + 1, beanName.length());
    }

}

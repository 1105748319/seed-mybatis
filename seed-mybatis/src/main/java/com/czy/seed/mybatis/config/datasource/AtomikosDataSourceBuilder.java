package com.czy.seed.mybatis.config.datasource;

import com.czy.seed.mybatis.config.exception.ConfigErrorException;
import com.czy.seed.mybatis.config.exception.DataSourceBuildException;
import com.czy.seed.mybatis.tool.NullUtil;
import com.czy.seed.mybatis.tool.SpringPropertiesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PLC on 2017/5/1.
 */
public class AtomikosDataSourceBuilder extends DataSourceBuilder {

    /**
     * 初始连接池信息
     *
     * @throws ClassNotFoundException
     */
    @Override
    protected void initPoolInfo() {
        if (SpringPropertiesUtil.containsKey("datasource.pool")) {
            String property = SpringPropertiesUtil.getProperty("datasource.pool");
            if (property.startsWith("atomikos")) {
                try {
                    poolType = DataSourcePoolType.valueOf(property);
                } catch (Exception e) {
                    throw new ConfigErrorException("不支持的连接池类型：" + property);
                }
            } else {
                poolType = DataSourcePoolType.atomikos_noxa;
            }
        } else {
            poolType = DataSourcePoolType.atomikos_noxa;
        }
        try {
            poolTypeClass = Class.forName(poolType.getPoolClass());
        } catch (ClassNotFoundException e) {
            throw new DataSourceBuildException(poolType.getPoolClass() + " is not found!");
        }
    }

    @Override
    public String getUrl(Map<String, Object> conf) {
        if (conf == null) {
            throw new IllegalArgumentException("datasource config info can not be null");
        } else {
            if (poolType == DataSourcePoolType.atomikos_xa) {
                Map<String, Object> xaProperties = (Map<String, Object>) conf.get("xaProperties");
                if (!(xaProperties).containsKey("url")) {
                    throw new IllegalArgumentException("datasource config must contains the key: url");
                } else {
                    return (String) xaProperties.get("url");
                }
            } else {
                return (String) conf.get("url");
            }

        }
    }

    @Override
    public Map<String, Object> getDataSourceConf(String prefix) {
        Map<String, Object> dataSourceConf = super.getDataSourceConf(prefix);
        dataSourceConf.put("uniqueResourceName", getDbName(prefix));
        if (poolType == DataSourcePoolType.atomikos_xa) {
            replaceConfig(dataSourceConf, "driverClassName", "xaDataSourceClassName");
            dataSourceConf.put("xaProperties", getXaProperties(dataSourceConf));
            configAdapterXa(dataSourceConf);
        } else {
            replaceConfig(dataSourceConf, "username", "user");
            configAdapterNoXa(dataSourceConf);
        }
        return dataSourceConf;
    }

    private String getDbName(String prefix) {
        if (NullUtil.isNotEmpty(prefix)) {
            if (prefix.startsWith(DEFAULT_DATASOURCE_PREFIX)) {
                return DATASOURCE_BEAN_PREFIX + MAIN_DATASOURCE_SUFFIX;
            } else {
                return DATASOURCE_BEAN_PREFIX + prefix.split("\\.")[2];
            }
        } else {
            throw new IllegalArgumentException("prefix can not be null");
        }
    }

    /**
     * 设置数据库连接信息
     * @param dataSourceConf 默认配置
     * @return 适配后的配置
     */
    private Map<String, Object> getXaProperties(Map<String, Object> dataSourceConf) {
        Map<String, Object> xaProperties = new HashMap<String, Object>();

        xaProperties.put("url", dataSourceConf.get("url"));
        dataSourceConf.remove("url");

        xaProperties.put("user", dataSourceConf.get("username"));
        dataSourceConf.remove("username");

        xaProperties.put("password", dataSourceConf.get("password"));
        dataSourceConf.remove("password");

        return xaProperties;
    }

    /**
     * 配置适配
     * @param dataSourceConf
     */
    private void configAdapterNoXa(Map<String, Object> dataSourceConf) {
        replaceConfig(dataSourceConf, "minIdle", "minPoolSize");
        replaceConfig(dataSourceConf, "maxIdle", "maxPoolSize");
        replaceConfig(dataSourceConf, "maxWait", "borrowConnectionTimeout");
        replaceConfig(dataSourceConf, "validationQuery", "testQuery");
        replaceConfig(dataSourceConf, "timeBetweenEvictionRunsMillis", "maintenanceInterval");
        dataSourceConf.remove("testOnBorrow");
        dataSourceConf.remove("testWhileIdle");
        dataSourceConf.remove("initialSize");
    }

    /**
     * 配置适配
     * @param dataSourceConf
     */
    private void configAdapterXa(Map<String, Object> dataSourceConf) {
        replaceConfig(dataSourceConf, "minIdle", "minPoolSize");
        replaceConfig(dataSourceConf, "maxIdle", "maxPoolSize");
        replaceConfig(dataSourceConf, "maxWait", "borrowConnectionTimeout");
        replaceConfig(dataSourceConf, "validationQuery", "testQuery");
        replaceConfig(dataSourceConf, "timeBetweenEvictionRunsMillis", "maintenanceInterval");
        dataSourceConf.remove("testOnBorrow");
        dataSourceConf.remove("testWhileIdle");
        dataSourceConf.remove("driverClassName");
        dataSourceConf.remove("initialSize");
    }

    /**
     * 属性替换
     * @param defaultDataSourceConf 配置项
     * @param source 原属性名称
     * @param target 现在属性名称
     */
    private void replaceConfig(Map<String, Object> defaultDataSourceConf, String source, String target) {
        Object sourceValue = defaultDataSourceConf.get(source);
        defaultDataSourceConf.put(target, sourceValue);
        defaultDataSourceConf.remove(source);
    }

}

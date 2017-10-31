package com.czy.seed.mybatis.config.datasource;

import com.czy.seed.mybatis.config.exception.DataSourceBuildException;
import com.czy.seed.mybatis.tool.SpringContextHelper;
import com.czy.seed.mybatis.tool.SpringPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by panlc on 2017-03-14.
 */
public class DataSourceBuilder {

    @Autowired
    public SpringContextHelper springContextHelper;

    private Map<String, String> datasourceDialect = new HashMap<String, String>();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 主数据源配置前缀
     */
    public static final String DEFAULT_DATASOURCE_PREFIX = "default.datasource.";

    /**
     * 其他数据源配置前缀
     */
    public static final String DYNAMIC_DATASOURCE_PREFIX = "dynamic.datasource.";

    public static final String DATASOURCE_BEAN_PREFIX = "datasource-";

    public static final String MAIN_DATASOURCE_SUFFIX = "default";
    /**
     * 主数据源名称
     */
    protected final String MAIN_DATASOURCE_NAME = DATASOURCE_BEAN_PREFIX + MAIN_DATASOURCE_SUFFIX;

    protected DataSourcePoolType poolType;    //连接池类型

    protected Class poolTypeClass;            //连接池初始化类

    public Map<String, String> getDatasourceDialect() {
        return datasourceDialect;
    }

    @PostConstruct
    public void calPoolType() throws ClassNotFoundException {
        initPoolInfo();
        registDataSources();
    }

    /**
     * 初始连接池信息
     *
     * @throws ClassNotFoundException
     */
    protected void initPoolInfo() {
        if (SpringPropertiesUtil.containsKey("datasource.pool")) {
            String property = SpringPropertiesUtil.getProperty("datasource.pool");
            poolType = DataSourcePoolType.valueOf(property);
        } else {
            poolType = DataSourcePoolType.druid;
        }
        try {
            poolTypeClass = Class.forName(poolType.getPoolClass());
        } catch (ClassNotFoundException e) {
            throw new DataSourceBuildException(poolType.getPoolClass() + " is not found!");
        }
    }

    /**
     * 注册数据源
     */
    private void registDataSources() {
        registerDefaultDataSources();
        registerDynamicDataSources();
    }

    /**
     * 注册主数据源
     */
    private void registerDefaultDataSources() {
        Map<String, Object> defaultDataSourceConf = getDefaultDataSourceConf();
        if (defaultDataSourceConf.keySet().size() == 0) {
            initTargetDefaultDataSource();
        } else {
            initConfigDefaultDataSource(defaultDataSourceConf);
        }
        setDialect(MAIN_DATASOURCE_NAME, defaultDataSourceConf);     //记录主数据源类型
    }

    private void initTargetDefaultDataSource() {
        String defaultKey = "default.datasource";
        if (!SpringPropertiesUtil.containsKey(defaultKey)) {
            throw new IllegalArgumentException("can not find the default datasource");  //TODO 更完善的提示信息
        } else {
            DataSource defaultDatasource = springContextHelper.getBeanById(SpringPropertiesUtil.getStringProperty(defaultKey));
            String url = null;
            try {
                url = defaultDatasource.getConnection().getMetaData().getURL();
            } catch (SQLException e) {
                logger.error("the datasource with the id{} in spring context can't be connected", SpringPropertiesUtil.getProperty(defaultKey), e);
            }
            setDialect(MAIN_DATASOURCE_NAME, url);     //记录主数据源类型
        }
    }

    public void initConfigDefaultDataSource(Map<String, Object> defaultDataSourceConf) {
        try {
            springContextHelper.addBean(poolTypeClass, MAIN_DATASOURCE_NAME,
                    defaultDataSourceConf, poolType.getInitMethod(), poolType.getDestroyMethod());  //注册主数据源
        } catch (Exception e) {
            throw new DataSourceBuildException("error occurred while build defaultDatasource", e);
        }
    }

    /**
     * 注册其他数据源
     */
    private void registerDynamicDataSources() {
        String[] dynamicDataSourceNames = getDynamicDataSourceNames();
        for (int i = 0; i < dynamicDataSourceNames.length; i++) {
            String dynamicDataSourceName = dynamicDataSourceNames[i];
            Map<String, Object> dynamicDataSourceConf = getDynamicDataSourceConf(dynamicDataSourceName);
            String beanId = DATASOURCE_BEAN_PREFIX + dynamicDataSourceName;
            springContextHelper.addBean(poolTypeClass, beanId,
                    dynamicDataSourceConf, poolType.getInitMethod(), poolType.getDestroyMethod());  //注册主数据源
            setDialect(beanId, dynamicDataSourceConf);     //记录其他数据源类型
        }
    }

    /**
     * 获取动态数据源名称
     *
     * @return
     */
    private String[] getDynamicDataSourceNames() {
        Map<String, Object> ctxPropertiesMap = SpringPropertiesUtil.getCtxPropertiesMap();
        Set<String> dbNames = new HashSet<String>();
        Set<String> configs = ctxPropertiesMap.keySet();
        for (String config : configs) {
            if (config.startsWith("dynamic.datasource")) {
                String[] split = config.split("\\.");
                dbNames.add(split[2].trim());
            }
        }
        String[] res = new String[dbNames.size()];
        return dbNames.toArray(res);
    }

    /**
     * 根据前缀获取配置信息
     *
     * @param prefix 指定的前缀，不可为''或null
     * @return 匹配的配置项
     */
    public Map<String, Object> getDataSourceConf(String prefix) {
        if (prefix == null || "".equals(prefix)) {
            throw new IllegalArgumentException("datasource config prefix can not be null or ''");
        }
        Map<String, Object> conf = new HashMap<String, Object>();
        Map<String, Object> ctxPropertiesMap = SpringPropertiesUtil.getCtxPropertiesMap();
        for (String key : ctxPropertiesMap.keySet()) {
            if (key.startsWith(prefix)) {
                conf.put(key.replace(prefix, ""), ctxPropertiesMap.get(key));
            }
        }
        if (conf.keySet().size() == 0) {
            logger.warn("datasource config with the prefix {} is empty", prefix);
        }
        return conf;
    }

    /**
     * 处理特殊数据库类弄的连接URL
     *
     * @param conf     数据库配置信息
     * @param identity 要处理的数据库类型
     * @throws UnsupportedEncodingException
     */
    public void dealUrlForSpecialDataSource(Map<String, Object> conf, IdentityDialect identity) {
        if (conf != null) {
            String val = getUrl(conf);
            IdentityDialect identityDialect = getIdentityDialect(val);
            if (identity == identityDialect) {
                String path = this.getClass().getResource("/").getPath();
                try {
                    path = URLDecoder.decode(path, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("errored while deal database url");
                }
                conf.put("url", val.replace("classpath:", path));
            }
        }
    }

    public String getUrl(Map<String, Object> conf) {
        String key = "url";
        if (conf.containsKey(key)) {
            String url = (String) conf.get(key);
            return url;
        }
        throw new RuntimeException("url is not defined!");
    }

    /**
     * 获取默认数据源配置
     *
     * @return
     */
    private Map<String, Object> getDefaultDataSourceConf() {
        return getDataSourceConfigForSpecialDataSource(DEFAULT_DATASOURCE_PREFIX);
    }

    /**
     * 获取动态数据源配置
     *
     * @param datasourceName 数据源名称
     * @return
     */
    private Map<String, Object> getDynamicDataSourceConf(String datasourceName) {
        return getDataSourceConfigForSpecialDataSource(DYNAMIC_DATASOURCE_PREFIX + datasourceName + ".");
    }

    public Map<String, Object> getDataSourceConfigForSpecialDataSource(String prefix) {
        Map<String, Object> dataSourceConf = getDataSourceConf(prefix);
        dealUrlForSpecialDataSource(dataSourceConf, IdentityDialect.SQLITE);
        return dataSourceConf;
    }


    /**
     * 获取数据库类型
     *
     * @param datasourceName
     * @return
     */
    public String getDialect(String datasourceName) {
        if (datasourceDialect.containsKey(datasourceName)) {
            return datasourceDialect.get(datasourceName);
        } else {
            throw new IllegalArgumentException("can not find a datasource with the name : " + datasourceName);
        }
    }

    /**
     * 设置数据源类型
     *
     * @param datasourceName 数据源名称
     * @param conf           数据库配置信息
     */
    protected void setDialect(String datasourceName, Map<String, Object> conf) {
        if (conf == null) {
            throw new IllegalArgumentException("datasource config info can not be null");
        } else {
            String url = getUrl(conf);
            if (url == null) {
                throw new IllegalArgumentException("datasource config must contains the key: url");
            } else {
                setDialect(datasourceName, url);
            }
        }
    }

    /**
     * 设置数据库类型
     *
     * @param datasourceName 数据源名称
     * @param url            数据库连接url
     */
    protected void setDialect(String datasourceName, String url) {
        datasourceDialect.put(datasourceName, getDBIdentityFromUrl(url));
    }

    /**
     * 获取数据库类型
     *
     * @param jdbcUrl 数据库连接URL
     * @return String
     */
    private String getDBIdentityFromUrl(String jdbcUrl) {
        return getIdentityDialect(jdbcUrl).name(); //TODO
    }

    /**
     * 获取数据库类型
     *
     * @param jdbcUrl 数据库连接URL
     * @return IdentityDialect
     */
    private IdentityDialect getIdentityDialect(String jdbcUrl) {
        if (jdbcUrl == null) {
            throw new IllegalArgumentException("数据库连接url不能为空");
        }
        String[] split = jdbcUrl.split("\\:");
        if (split.length < 3) {
            throw new IllegalArgumentException("数据库连接url不符合格式：jdbc:[xx]:[xxx]");
        }
        String identity = split[1]; //针对jdbc:mysql://localhost:3306/test类url获取数据库类型
        IdentityDialect identityDialect;
        try {
            identityDialect = IdentityDialect.valueOf(identity.toUpperCase());//验证数据库类型是否获取正确
        } catch (Exception e) {
            identity = split[2];    //针对jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=[database]类url获取数据库类型
            try {
                identityDialect = IdentityDialect.valueOf(identity.toUpperCase());   //验证数据库类型是否获取正确h
            } catch (Exception e2) {
                throw new IllegalArgumentException("只支持以下数据库：" + IdentityDialect.values());
            }
        }
        return identityDialect;
    }

}

package com.czy.seed.mybatis.sql.entity;

import com.czy.seed.mybatis.sql.util.SqlStringUtil;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import javax.persistence.Table;
import java.util.*;

/**
 * 数据库表
 *
 * @author liuzh
 */
public class EntityTable {
    private String name;
    private String catalog;
    private String schema;
    private String orderByClause;
    private String baseSelect;
    //实体类 => 全部列属性
    private Set<EntityColumn> entityClassColumns;
    //实体类 => 主键信息
    private Set<EntityColumn> entityClassPKColumns;
    //一对一映射属性
    private Set<MybatisAssociation> mybatisAssociations = new HashSet<MybatisAssociation>();
    //一对多映射属性
    private Set<MybatisConnection> mybatisConnections = new HashSet<MybatisConnection>();

    //useGenerator包含多列的时候需要用到
    private List<String> keyProperties;
    private List<String> keyColumns;
    //resultMap对象
    private ResultMap resultMap;
    //属性和列对应
    protected Map<String, EntityColumn> propertyMap;
    //类
    private Class<?> entityClass;
    //类名
    private String entityClassName;
    //接口类
    private Class<?> mapperClass;
    //接口类名
    private String mapperClassName;
    private String cache;
    /**
     * 增加映射关系，去除重复数据
     * @param mybatisAssociation 要增加的映射关系实体
     */
    public void addMybatisAssociations(MybatisAssociation mybatisAssociation) {
        if (mybatisAssociations.size() == 0) {
            mybatisAssociations.add(mybatisAssociation);
        } else {    //去重
            Set<MybatisAssociation> mybatisAssociations_temp = new HashSet<MybatisAssociation>();
            for (MybatisAssociation has : mybatisAssociations) {
                if (has.getColumns().equals(mybatisAssociation.getColumns())
                        && has.getTargetTableName().equals(mybatisAssociation.getTargetTableName())) {
                    has.getResultMap().addAll(mybatisAssociation.getResultMap());
                } else {
                    mybatisAssociations_temp.add(mybatisAssociation);
                }
            }
            mybatisAssociations.addAll(mybatisAssociations_temp);
        }
    }

    /**
     * 增加映射关系，去除重复数据
     * @param mybatisConnection 要增加的映射关系实体
     */
    public void addMybatisConnections(MybatisConnection mybatisConnection) {
        if (mybatisConnections.size() == 0) {
            mybatisConnections.add(mybatisConnection);
        } else {    //去重
            Set<MybatisConnection> mybatisConnections_temp = new HashSet<MybatisConnection>();
            for (MybatisAssociation has : mybatisConnections) {
                if (has.getColumns().equals(mybatisConnection.getColumns())
                        && has.getTargetTableName().equals(mybatisConnection.getTargetTableName())) {
                    has.getResultMap().addAll(mybatisConnection.getResultMap());
                } else {
                    mybatisConnections_temp.add(mybatisConnection);
                }
            }
            mybatisConnections.addAll(mybatisConnections_temp);
        }
    }

    public Set<MybatisAssociation> getMybatisAssociations() {
        return mybatisAssociations;
    }

    public void setMybatisAssociations(Set<MybatisAssociation> mybatisAssociations) {
        this.mybatisAssociations = mybatisAssociations;
    }

    public EntityTable(Class<?> mapperClass, Class<?> entityClass) {
        this.mapperClass = mapperClass;
        this.entityClass = entityClass;
        if (mapperClass != null) {
            this.mapperClassName = mapperClass.getName();
        }
        this.entityClassName = entityClass.getName();
    }

    public void setTable(Table table) {
        this.name = table.name();
        this.catalog = table.catalog();
        this.schema = table.schema();
    }

    public Class<?> getMapperClass() {
        return mapperClass;
    }

    public void setMapperClass(Class<?> mapperClass) {
        this.mapperClass = mapperClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    private void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Set<MybatisConnection> getMybatisConnections() {
        return mybatisConnections;
    }

    public void setMybatisConnections(Set<MybatisConnection> mybatisConnections) {
        this.mybatisConnections = mybatisConnections;
    }

    public void setKeyColumns(List<String> keyColumns) {
        this.keyColumns = keyColumns;
    }

    public void setKeyProperties(List<String> keyProperties) {
        this.keyProperties = keyProperties;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getBaseSelect() {
        return baseSelect;
    }

    public void setBaseSelect(String baseSelect) {
        this.baseSelect = baseSelect;
    }

    public String getPrefix() {
        if (SqlStringUtil.isNotEmpty(catalog)) {
            return catalog;
        }
        if (SqlStringUtil.isNotEmpty(schema)) {
            return schema;
        }
        return "";
    }

    public Set<EntityColumn> getEntityClassColumns() {
        return entityClassColumns;
    }

    public void setEntityClassColumns(Set<EntityColumn> entityClassColumns) {
        this.entityClassColumns = entityClassColumns;
    }

    public Set<EntityColumn> getEntityClassPKColumns() {
        return entityClassPKColumns;
    }

    public void setEntityClassPKColumns(Set<EntityColumn> entityClassPKColumns) {
        this.entityClassPKColumns = entityClassPKColumns;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public String getMapperClassName() {
        return mapperClassName;
    }

    public void setMapperClassName(String mapperClassName) {
        this.mapperClassName = mapperClassName;
    }

    public String[] getKeyProperties() {
        if (keyProperties != null && keyProperties.size() > 0) {
            return keyProperties.toArray(new String[]{});
        }
        return new String[]{};
    }

    public void setKeyProperties(String keyProperty) {
        if (this.keyProperties == null) {
            this.keyProperties = new ArrayList<String>();
            this.keyProperties.add(keyProperty);
        } else {
            this.keyProperties.add(keyProperty);
        }
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String[] getKeyColumns() {
        if (keyColumns != null && keyColumns.size() > 0) {
            return keyColumns.toArray(new String[]{});
        }
        return new String[]{};
    }

    public void setKeyColumns(String keyColumn) {
        if (this.keyColumns == null) {
            this.keyColumns = new ArrayList<String>();
            this.keyColumns.add(keyColumn);
        } else {
            this.keyColumns.add(keyColumn);
        }
    }

    /**
     * 生成当前实体的resultMap对象
     *
     * @param configuration
     * @return
     */
    public ResultMap getResultMap(Configuration configuration) {
        if (this.resultMap != null) {
            return this.resultMap;
        }
        if (entityClassColumns == null || entityClassColumns.size() == 0) {
            return null;
        }
        List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
        for (EntityColumn entityColumn : entityClassColumns) {
            ResultMapping.Builder builder = new ResultMapping.Builder(configuration, entityColumn.getProperty(), entityColumn.getColumn(), entityColumn.getJavaType());
            if (entityColumn.getJdbcType() != null) {
                builder.jdbcType(entityColumn.getJdbcType());
            }
            if (entityColumn.getTypeHandler() != null) {
                try {
                    builder.typeHandler(entityColumn.getTypeHandler().newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            List<ResultFlag> flags = new ArrayList<ResultFlag>();
            if (entityColumn.isId()) {
                flags.add(ResultFlag.ID);
            }
            builder.flags(flags);
            resultMappings.add(builder.build());
        }
        ResultMap.Builder builder = new ResultMap.Builder(configuration, "BaseMapperResultMap", this.entityClass, resultMappings, true);
        this.resultMap = builder.build();
        return this.resultMap;
    }

    /**
     * 初始化 - Example 会使用
     */
    public void initPropertyMap() {
        propertyMap = new HashMap<String, EntityColumn>(getEntityClassColumns().size());
        for (EntityColumn column : getEntityClassColumns()) {
            propertyMap.put(column.getProperty(), column);
        }
    }

    public Map<String, EntityColumn> getPropertyMap() {
        return propertyMap;
    }
}

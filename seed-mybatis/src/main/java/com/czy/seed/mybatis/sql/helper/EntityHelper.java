/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.czy.seed.mybatis.sql.helper;

import com.czy.seed.mybatis.config.datasource.IdentityDialect;
import com.czy.seed.mybatis.config.mybatis.annotations.Cache;
import com.czy.seed.mybatis.config.mybatis.annotations.ColumnType;
import com.czy.seed.mybatis.config.mybatis.annotations.One2Many;
import com.czy.seed.mybatis.config.mybatis.annotations.One2One;
import com.czy.seed.mybatis.sql.entity.*;
import com.czy.seed.mybatis.sql.util.SqlStringUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;


import javax.persistence.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 实体类工具类 - 处理实体和数据库表以及字段关键的一个类
 * <p/>
 * <p>项目地址 : <a href="https://github.com/abel533/Mapper" target="_blank">https://github.com/abel533/Mapper</a></p>
 *
 * @author liuzh
 */
public class EntityHelper {

    /**
     * 实体类 => 表对象
     */
    private static final Map<Class<?>, EntityTable> entityTableMap = new HashMap<Class<?>, EntityTable>();

    /**
     * 获取全部列
     *
     * @param entityClass
     * @return
     */
    public static Set<EntityColumn> getColumns(Class<?> entityClass) {
        return getEntityTableByMapperClass(entityClass).getEntityClassColumns();
    }

    /**
     * 获取主键信息
     *
     * @param entityClass
     * @return
     */
    public static Set<EntityColumn> getPKColumns(Class<?> entityClass) {
        return getEntityTableByMapperClass(entityClass).getEntityClassPKColumns();
    }

    /**
     * 获取查询的Select
     *
     * @param entityClass
     * @return
     */
    public static String getSelectColumns(Class<?> entityClass) {
        EntityTable entityTable = getEntityTableByMapperClass(entityClass);
        if (entityTable.getBaseSelect() != null) {
            return entityTable.getBaseSelect();
        }
        Set<EntityColumn> columnList = getColumns(entityClass);
        StringBuilder selectBuilder = new StringBuilder();
        boolean skipAlias = Map.class.isAssignableFrom(entityClass);
        for (EntityColumn entityColumn : columnList) {
            selectBuilder.append(entityColumn.getColumn());
            if (!skipAlias && !entityColumn.getColumn().equalsIgnoreCase(entityColumn.getProperty())) {
                //不等的时候分几种情况，例如`DESC`
                if (entityColumn.getColumn().substring(1, entityColumn.getColumn().length() - 1).equalsIgnoreCase(entityColumn.getProperty())) {
                    selectBuilder.append(",");
                } else {
                    selectBuilder.append(" AS ").append(entityColumn.getProperty()).append(",");
                }
            } else {
                selectBuilder.append(",");
            }
        }
        entityTable.setBaseSelect(selectBuilder.substring(0, selectBuilder.length() - 1));
        return entityTable.getBaseSelect();
    }

    /**
     * 根据实体类型获取表对象
     * @param entityClass 实体类类型
     * @return
     */
    public static EntityTable getEntityTableByEntityClass(Class<?> entityClass) {
        return buildEntityTable(null, entityClass);

    }

    /**
     * 根据mapper接口类型获取表对象
     *
     * @param mapperClass 接口类类型
     */
    public static EntityTable getEntityTableByMapperClass(Class<?> mapperClass) {
        Style style = Style.camelhumpAndUppercase;  //TODO 可设置
        Class<?> entityClass = getEntityClass(mapperClass);
        //创建EntityTable
        return buildEntityTable(mapperClass, entityClass);
    }

    private static EntityTable buildEntityTable(Class<?> mapperClass, Class<?> entityClass) {
        Style style = Style.camelhumpAndUppercase;  //TODO 可设置
        EntityTable entityTable = null;
        entityTable = new EntityTable(mapperClass, entityClass);
        //设置对应表名，可以通过stye控制
        entityTable.setName(SqlStringUtil.convertByStyle(entityClass.getSimpleName(), style));

        //处理@Table注解
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (!table.name().equals("")) {
                entityTable.setTable(table);
            }
        }

        //处理@Cache注解
        if (entityClass.isAnnotationPresent(Cache.class)) {
            Cache cache = entityClass.getAnnotation(Cache.class);
            if (cache.cacheType() == Cache.CacheType.MYBATIS) {
                entityTable.setCache(cache.cacheType().name());
            }
        }

        entityTable.setEntityClassColumns(new LinkedHashSet<EntityColumn>());
        entityTable.setEntityClassPKColumns(new LinkedHashSet<EntityColumn>());

        //处理所有列
        List<EntityField> fields = null;

        fields = FieldHelper.getAll(entityClass);   //TODO 待扩展

        for (EntityField field : fields) {
            processField(entityTable, style, field);
        }
        //当pk.size=0的时候使用所有列作为主键
        if (entityTable.getEntityClassPKColumns().size() == 0) {
            entityTable.setEntityClassPKColumns(entityTable.getEntityClassColumns());
        }
        entityTable.initPropertyMap();
//        entityTableMap.put(entityClass, entityTable);

        return entityTable;
    }

    /**
     * 获取mapper接口对应的实体类
     * @param mapperClass
     * @return
     */
    public static Class<?> getEntityClass(final Class<?> mapperClass) {
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType() == mapperClass || ((Class<?>) t.getRawType()).isAssignableFrom(mapperClass)) {
                    Class<?> returnType = (Class<?>) t.getActualTypeArguments()[0];
                    return returnType;
                }
            }
        }
        throw new RuntimeException("无法获取Mapper<T>泛型类型:" + mapperClass.getName());
    }

    /**
     * 处理数据列
     *
     * @param entityTable
     * @param style
     * @param field
     */
    private static void processField(EntityTable entityTable, Style style, EntityField field) {
        //排除字段
        if (field.isAnnotationPresent(Transient.class)) {
            return;
        }
        //处理一对一关系
        if (field.isAnnotationPresent(One2One.class)) {
            One2One annotation = field.getAnnotation(One2One.class);
            MybatisAssociation association = new MybatisAssociation(entityTable.getName(), field.getName(),
                    field.getJavaType(), annotation.fields(), annotation.columns(), annotation.join());
            entityTable.addMybatisAssociations(association);
            return;
        }
        //处理一对多关系
        if (field.isAnnotationPresent(One2Many.class)) {
            One2Many annotation = field.getAnnotation(One2Many.class);
            if (List.class.isAssignableFrom(field.getJavaType())) {
                Type gt = field.getField().getGenericType();
                if(gt instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) gt;
                    Class genericClazz = (Class)pt.getActualTypeArguments()[0];
                    MybatisConnection connection = new MybatisConnection(entityTable.getName(), field.getName(),
                            genericClazz, annotation.fields(), annotation.columns(), annotation.join());
                    entityTable.addMybatisConnections(connection);
                }
            }
            return;
        }
        //Id
        EntityColumn entityColumn = new EntityColumn(entityTable);
        if (field.isAnnotationPresent(Id.class)) {
            entityColumn.setId(true);
        }
        //Column
        String columnName = dealColumn(field, entityColumn);
        //ColumnType
        columnName = dealColumnType(field, entityColumn, columnName, style);

        entityColumn.setProperty(field.getName());
        entityColumn.setColumn(columnName);
        entityColumn.setJavaType(field.getJavaType());
        //OrderBy
        dealOrderBy(field, entityColumn);
        //主键策略 - Oracle序列，MySql自动增长，UUID
        dealKey(entityTable, field, entityColumn);
        entityTable.getEntityClassColumns().add(entityColumn);
        if (entityColumn.isId()) {
            entityTable.getEntityClassPKColumns().add(entityColumn);
        }
    }

    /**
     * 处理Column注解
     * @param field
     * @param entityColumn
     * @return
     */
    private static String dealColumn(EntityField field, EntityColumn entityColumn) {
        String columnName = null;
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            columnName = column.name();
            entityColumn.setUpdatable(column.updatable());
            entityColumn.setInsertable(column.insertable());
        }
        return columnName;
    }

    /**
     * 主键策略处理
     * @param entityTable
     * @param field
     * @param entityColumn
     */
    private static void dealKey(EntityTable entityTable, EntityField field, EntityColumn entityColumn) {
        if (field.isAnnotationPresent(SequenceGenerator.class)) {
            SequenceGenerator sequenceGenerator = field.getAnnotation(SequenceGenerator.class);
            if (sequenceGenerator.sequenceName().equals("")) {
                throw new RuntimeException(entityTable.getEntityClass() + "字段" + field.getName() + "的注解@SequenceGenerator未指定sequenceName!");
            }
            entityColumn.setSequenceName(sequenceGenerator.sequenceName());
        } else if (field.isAnnotationPresent(GeneratedValue.class)) {
            GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
            if (generatedValue.generator().equals("UUID")) {
                entityColumn.setUuid(true);
            } else if (generatedValue.generator().equals("JDBC")) {
                entityColumn.setIdentity(true);
                entityColumn.setGenerator("JDBC");
                entityTable.setKeyProperties(entityColumn.getProperty());
                entityTable.setKeyColumns(entityColumn.getColumn());
            } else {
                //允许通过generator来设置获取id的sql,例如mysql=CALL IDENTITY(),hsqldb=SELECT SCOPE_IDENTITY()
                //允许通过拦截器参数设置公共的generator
                if (generatedValue.strategy() == GenerationType.IDENTITY) {
                    //mysql的自动增长
                    entityColumn.setIdentity(true);
                    if (!generatedValue.generator().equals("")) {
                        String generator = null;
                        IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(generatedValue.generator());
                        if (identityDialect != null) {
                            generator = identityDialect.getIdentityRetrievalStatement();
                        } else {
                            generator = generatedValue.generator();
                        }
                        entityColumn.setGenerator(generator);
                    }
                } else {
                    throw new RuntimeException(field.getName()
                            + " - 该字段@GeneratedValue配置只允许以下几种形式:" +
                            "\n1.全部数据库通用的@GeneratedValue(generator=\"UUID\")" +
                            "\n2.useGeneratedKeys的@GeneratedValue(generator=\\\"JDBC\\\")  " +
                            "\n3.类似mysql数据库的@GeneratedValue(strategy=GenerationType.IDENTITY[,generator=\"Mysql\"])");
                }
            }
        }
    }

    /**
     * 处理OrderBy注解
     * @param field
     * @param entityColumn
     */
    private static void dealOrderBy(EntityField field, EntityColumn entityColumn) {
        if (field.isAnnotationPresent(OrderBy.class)) {
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            if (orderBy.value().equals("")) {
                entityColumn.setOrderBy("ASC");
            } else {
                entityColumn.setOrderBy(orderBy.value());
            }
        }
    }

    /**
     * 处理ColumnType注解
     * @param field
     * @param entityColumn
     * @param columnName
     */
    private static String dealColumnType(EntityField field, EntityColumn entityColumn, String columnName, Style style) {
        if (field.isAnnotationPresent(ColumnType.class)) {
            ColumnType columnType = field.getAnnotation(ColumnType.class);
            //column可以起到别名的作用
            if (SqlStringUtil.isEmpty(columnName) && SqlStringUtil.isNotEmpty(columnType.column())) {
                columnName = columnType.column();
            }
            if (columnType.jdbcType() != JdbcType.UNDEFINED) {
                entityColumn.setJdbcType(columnType.jdbcType());
            }
            if (columnType.typeHandler() != UnknownTypeHandler.class) {
                entityColumn.setTypeHandler(columnType.typeHandler());
            }
        }
        if (field.getJavaType() == java.lang.String.class) {
            if (entityColumn.getJdbcType() == null) {
                entityColumn.setJdbcType(JdbcType.VARCHAR);
            }
        }
        //字段名处理名
        if (SqlStringUtil.isEmpty(columnName)) {
            columnName = SqlStringUtil.convertByStyle(field.getName(), style);
        }
        return columnName;
    }

}
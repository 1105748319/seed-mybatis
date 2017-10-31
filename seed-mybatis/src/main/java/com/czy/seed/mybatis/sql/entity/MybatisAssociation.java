package com.czy.seed.mybatis.sql.entity;

import com.czy.seed.mybatis.config.mybatis.annotations.Join;
import com.czy.seed.mybatis.sql.helper.EntityHelper;
import com.czy.seed.mybatis.tool.NullUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by panlc on 2017-03-28.
 */
public class MybatisAssociation {

//    public MybatisAssociation(@NotNull String mainTableName, String property, Class<?>target, One2One annotation) {
//        this(mainTableName, property, target, annotation.fields(), annotation.columns(), annotation.join());
//    }

    /**
     * @param mainTableName   主查询表名
     * @param property    查询数据映射字段
     * @param targetClass 关联实体类型
     * @param fields      要指定查询的字段
     * @param columns     关联查询条件
     */
    public MybatisAssociation(String mainTableName, String property, Class<?> targetClass,
                              String fields, String columns, Join join) {
        paramsCheck(mainTableName, property, targetClass, columns);
        this.property = property;
        this.join = join;
        this.columns = columns;
        this.mainTableName = mainTableName;
        this.targetEntityTable = EntityHelper.getEntityTableByEntityClass(targetClass);
        targetTableName = targetEntityTable.getName();
        initResultMap(fields);
        joinCondition(columns);
    }



    /**
     * 参数检查
     *
     * @param mainTableName
     * @param property
     * @param targetClass
     * @param columns
     */
    private void paramsCheck(String mainTableName, String property, Class<?> targetClass, String columns) {
        if (mainTableName == null) {
            throw new IllegalArgumentException("param \"mainTableName\" can't be null!");
        }
        if (targetClass == null) {
            throw new IllegalArgumentException("param \"targetClass\" can't be null!");
        }
        if (NullUtil.isEmpty(property)) {
            throw new IllegalArgumentException("param \"property\" can't be null or empty!");
        }
        if (NullUtil.isEmpty(columns)) {
            throw new IllegalArgumentException("param \"columns\" can't be null or empty!");
        }
    }

    /**
     * 关联查询表名
     */
    private String targetTableName;

    /**
     * 映射字段
     */
    private String property;

    /**
     * association查询列
     */
    private Set<EntityColumn> resultMap;

    private String mainTableName;

    private EntityTable targetEntityTable;

    private String columns;

    /**
     * 关联关系
     */
    private Join join;

    public String getProperty() {
        return property;
    }

    public Set<EntityColumn> getResultMap() {
        return resultMap;
    }

    public String getJoinCondition() {
        return joinCondition;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public String getJoin() {
        return join.name().toLowerCase();
    }

    public EntityTable getTargetEntityTable() {
        return targetEntityTable;
    }

    public String getColumns() {
        return columns;
    }
/**
     * 指定的查询列
     */

    /**
     * 关联查询条件
     */
    private String joinCondition;

    /**
     * 初始化resultMap
     *
     * @param fields 指定的选择列
     */
    private void initResultMap(String fields) {
        resultMap = new HashSet<EntityColumn>();
        Map<String, EntityColumn> propertyMap = targetEntityTable.getPropertyMap();
        if (NullUtil.isNotEmpty(fields)) {
            String[] split = fields.split(",");
            for (String field : split) {
                if (propertyMap.containsKey(field)) {
                    resultMap.add(propertyMap.get(field));
                } else {
                    throw new RuntimeException("实体类:" + targetEntityTable.getEntityClassName() + "不包含名为" + property + "的属性!");
                }
            }
        } else {
            resultMap.addAll(targetEntityTable.getEntityClassColumns());
        }
    }

    /**
     * 初始化连接条件
     *
     * @param columns 连接条件参数，格式：id=test_id, name=test_name
     */
    private void joinCondition(String columns) {
        String[] split = columns.split(",");
        StringBuffer sb = new StringBuffer(join.name());
        sb.append(" join ").append(targetTableName).append(" ").append(targetTableName).append("_").append(property).append(" on ");
        for (String col : split) {
            String[] condition = anaCondition(col);
            sb.append(mainTableName).append(".").append(condition[0]).append(" = ");
            sb.append(targetTableName).append("_").append(property).append(".").append(condition[1]);
            sb.append(" and ");
        }
        joinCondition = sb.substring(0,sb.length() - 4).toUpperCase();
    }

    /**
     * 拆分关联查询条件
     *
     * @param column
     * @return
     */
    private String[] anaCondition(String column) {
        String[] condition = new String[2];
        int i = column.indexOf("=");
        if (i == -1) {
            throw new IllegalArgumentException("column format must accord with 'xxx=xxx'");
        } else {
            condition[0] = column.substring(0, i);
            condition[1] = column.substring(i + 1, column.length());
        }
        return condition;
    }


}

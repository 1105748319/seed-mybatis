package com.czy.seed.mybatis.sql.util;


import com.czy.seed.mybatis.base.QueryParams;
import com.czy.seed.mybatis.sql.entity.EntityField;
import com.czy.seed.mybatis.sql.helper.FieldHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

/**
 * OGNL静态方法
 * <p>
 * Created by panlc on 2017-03-21.
 */
public abstract class MybatisColumnsOGNL {

    private static Logger logger = LoggerFactory.getLogger(MybatisColumnsOGNL.class);

    /**
     * 是否包含自定义查询列
     *
     * @param parameter
     * @return
     */
    public static boolean hasSelectColumns(Object parameter) {
        if (parameter != null && parameter instanceof QueryParams) {
            QueryParams Params = (QueryParams) parameter;
            if (Params.getSelectColumns() != null && Params.getSelectColumns().size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不包含自定义查询列
     *
     * @param parameter
     * @return
     */
    public static boolean hasNoSelectColumns(Object parameter) {
        return !hasSelectColumns(parameter);
    }

    /**
     * 判断参数的各字段是否有值
     * @param record
     * @return
     * @throws IllegalAccessException
     */
    public static boolean hasValue(Object record) throws IllegalAccessException {
        boolean res = false;
        if (record == null) {
            throw new IllegalArgumentException("record can't be null!");
        } else {
            List<EntityField> all = FieldHelper.getAll(record.getClass());
            for (EntityField entityField : all) {
                Field field = entityField.getField();
                if (field != null) {
                    field.setAccessible(true);
                    Object o = field.get(record);
                    if (o != null) {
                        res = true;
                        break;
                    }
                }
            }
        }
        if (!res) {
            logger.error("All fields in the record(type:{}) are null! This is not allowed!", record.getClass());
            throw new IllegalArgumentException("All fields in the record are null! This is not allowed!");
        }
        return res;
    }

}

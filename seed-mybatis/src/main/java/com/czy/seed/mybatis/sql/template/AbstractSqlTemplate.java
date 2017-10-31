package com.czy.seed.mybatis.sql.template;

import com.czy.seed.mybatis.sql.entity.EntityTable;
import com.czy.seed.mybatis.sql.helper.EntityHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by panlc on 2017-03-20.
 */
public abstract class AbstractSqlTemplate {

    /**
     * 生成mybatis需要的mapper.xml文件
     * @param mapperClass 实体类
     * @return  mapper.xml
     */
    public InputStream buildSqlFile(Class<?> mapperClass) {
        EntityTable entityTable = anayEntity(mapperClass);
        return sqlParse(entityTable);
    }

    /**
     * sql生成模板路
     * @return 模板名称
     */
    public abstract String getSqlTemplateName();

    /**
     * 解析实体，获得对应的表结构
     * @param mapperClass mapper类型
     * @return EntityTable
     */
    public EntityTable anayEntity(Class<?> mapperClass) {
        return EntityHelper.getEntityTableByMapperClass(mapperClass);
    }

    /**
     * 根据表结构生成默认mapper.xml文件。
     * @param entityTable
     * @return InputStream
     */
    private InputStream sqlParse(EntityTable entityTable) {
        String process = FreeMarkerUtil.process(getSqlTemplateName(), entityTable);
//        InputStream inputStream = new ByteArrayInputStream(process.getBytes());
        InputStream inputStream = new ByteArrayInputStream(process.getBytes(Charset.forName("UTF-8")));
        return inputStream;
    }

}

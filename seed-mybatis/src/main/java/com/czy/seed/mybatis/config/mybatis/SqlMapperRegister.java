package com.czy.seed.mybatis.config.mybatis;

import com.czy.seed.mybatis.base.Procedure;
import com.czy.seed.mybatis.sql.template.AbstractSqlTemplate;
import com.czy.seed.mybatis.sql.template.ProcedureSqlTemplate;
import com.czy.seed.mybatis.sql.template.SqlTemplateFactory;
import com.czy.seed.mybatis.tool.SpringContextHelper;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 动态注册SQL语句工具类
 * Created by panlc on 2017-03-20.
 */
public class SqlMapperRegister {

    private static Logger logger = LoggerFactory.getLogger(SqlMapperRegister.class);

    /**
     * 注册SQL至Mybatis
     *
     * @param mapperClass mapper接口类
     * @param sqlSessionFactoryBeanName sqlSessionFactory名称
     */
    public static void registerSqlMapper(Class<?> mapperClass, String sqlSessionFactoryBeanName) {
        AbstractSqlTemplate sqlTemplate = SqlTemplateFactory.getSqlTemplate(sqlSessionFactoryBeanName);
        InputStream inputStream = sqlTemplate.buildSqlFile(mapperClass);
        registerSqlMapper(inputStream, mapperClass.getName(), sqlSessionFactoryBeanName);
    }

    /**
     * 注册sql至mybatis
     * @param inputStream SQL文件输入流
     * @param mapperName mapper类全名
     * @param sqlSessionFactoryBeanName sqlSessionFactory名称
     */
    private static void registerSqlMapper(InputStream inputStream, String mapperName, String sqlSessionFactoryBeanName) {
        SqlSessionFactory sqlSessionFactory = SpringContextHelper.getBeanById(sqlSessionFactoryBeanName);
        Configuration configuration = sqlSessionFactory.getConfiguration();
        try {
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration, mapperName + "_AUTO_GEN", configuration.getSqlFragments());
            xmlMapperBuilder.parse();
        } finally {
            ErrorContext.instance().reset();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("can't close the xml inputStream after register a xml file to mybatis");
            throw new RuntimeException("can't close the xml inputStream after register a xml file to mybatis");
        }
    }

    /**
     * 注册存储过程
     * @param procedure
     */
    public static void registerProcedure(Procedure procedure, String sqlSessionFactoryBeanName) {
        InputStream inputStream = ProcedureSqlTemplate.createSqlInputStream(procedure);
        registerSqlMapper(inputStream, procedure.getMapperName() + "_procedure_" + procedure.getProcedureName(), sqlSessionFactoryBeanName);
    }

}

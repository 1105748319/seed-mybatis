package com.czy.seed.mybatis.base;

import com.czy.seed.mybatis.config.mybatis.SqlMapperRegister;
import com.czy.seed.mybatis.config.mybatis.annotations.AutoMapper;
import com.czy.seed.mybatis.tool.SpringContextHelper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by panlc on 2017-04-17.
 */
@Component
public class ProcedureExecutor {

    /**
     * 全局存储过程注册信息
     */
    private static Set<String> registeredProcedureName = new HashSet<String>();

    private Logger logger = LoggerFactory.getLogger(ProcedureExecutor.class);

    /**
     * @param procedure
     * @throws Exception
     */
    public void execute(Procedure procedure) throws Exception {
        String sqlSessionFactoryName = getSqlSessionFactoryName(procedure.getBaseMapper());
        SqlSessionFactory sqlSessionFactoryBean = SpringContextHelper.getBeanById(sqlSessionFactoryName);
        if (!isRegistered(procedure)) {

            SqlMapperRegister.registerProcedure(procedure, sqlSessionFactoryName);
            registeredProcedureName.add(buildKey(procedure));
        }
        tryExecute(procedure, sqlSessionFactoryBean);
    }

    /**
     * 判断存储过程是否已经注册 已注册：true，否则返回：false
     *
     * @param procedure
     * @return
     */
    private boolean isRegistered(Procedure procedure) {
        String key = buildKey(procedure);
        if (registeredProcedureName.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 构建存储过程标识
     *
     * @param procedure
     * @return
     */
    private String buildKey(Procedure procedure) {
        StringBuffer sb = new StringBuffer();
        sb.append(procedure.getMapperName())
                .append(".")
                .append(procedure.getProcedureName());
        return sb.toString();
    }

    /**
     * 执行存储过程
     *
     * @param procedure
     * @throws Exception
     */
    private void tryExecute(final Procedure procedure, SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //执行存储过程
//        sqlSession.select(buildKey(procedure), procedure.getInParams(), new ResultHandler() {
//            @Override
//            public void handleResult(ResultContext resultContext) {
//                procedure.setResult(resultContext.getResultObject());
//            }
//        });


        List<Object> objects = sqlSession.selectList(buildKey(procedure), procedure.getInParams());
        System.out.println(objects);

        try {
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("error occurred while execute procedure: {}", procedure.getProcedureName(), e);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 获取要注册的mapper所在的SqlSessionFactory名称
     * @param clazz
     * @return
     */
    public String getSqlSessionFactoryName(Class<?> clazz) {
        return "sqlSessionFactory-" + getDataBaseName(clazz);
    }


    /**
     * 获取数据名称
     * @param clazz
     * @return
     */
    private String getDataBaseName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(AutoMapper.class)) {
            AutoMapper autoMapper = clazz.getAnnotation(AutoMapper.class);
            String value = autoMapper.value();
            return value;
        }
        throw new IllegalArgumentException(clazz.getName() + "must be annotationed by AutoMapper.class ");
    }

}

//package com.czy.seed.mybatis.service;
//
//import com.czy.seed.mybatis.base.Procedure;
//import com.czy.seed.mybatis.base.ProcedureExecutor;
//import com.czy.seed.mybatis.entity.TestEntity;
//import com.czy.seed.mybatis.mapper.MySqlMapper;
//import com.czy.seed.mybatis.tool.SpringContextHelper;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * Created by panlc on 2017-03-31.
// */
//@Service
//@EnableTransactionManagement
//public class MysqlService {
//
//    @Resource
//    private MySqlMapper mySqlMapper;
//
//    @Resource
//    private ProcedureExecutor procedureExecutor;
//
//    @Transactional(value = "tm-default", rollbackFor = Exception.class)
//    public void testRollback() throws Exception {
//        TestEntity testEntity = new TestEntity();
//        testEntity.setName("Transactional Test");
//        TestEntity testEntity1 = new TestEntity();
//        testEntity1.setName("Transactional Test2");
//        mySqlMapper.insert(testEntity);
//        mySqlMapper.insert(testEntity1);
//
//        Procedure procedure = new Procedure(MySqlMapper.class);
//        procedure.addInParams("a", 1);
//        procedure.setProcedureName("pro3");
//
//        procedureExecutor.execute(procedure);
//        procedureExecutor.execute(procedure);
//
//        Object result = procedure.getResult();
//        System.out.println(result);
//
//        String sqlSessionFactoryName = procedureExecutor.getSqlSessionFactoryName(MySqlMapper.class);
//        SqlSessionFactory sqlSessionFactoryBean = SpringContextHelper.getBeanById(sqlSessionFactoryName);
//        SqlSession sqlSession = sqlSessionFactoryBean.openSession();
//    }
//
//    @Transactional(value = "tm-default", rollbackFor = Exception.class)
//    public void insertList(List<TestEntity> recordList) {
//        mySqlMapper.insertList(recordList);
//    }
//
//    @Transactional(value = "tm-default", rollbackFor = Exception.class)
//    public void insertList2(List<TestEntity> recordList) {
//        for (TestEntity t : recordList) {
//            mySqlMapper.insert(t);
//        }
//    }
//
//}

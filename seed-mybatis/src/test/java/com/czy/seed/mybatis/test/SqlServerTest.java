//package com.czy.seed.mybatis.test;
//
//import com.czy.seed.mybatis.base.QueryParams;
//import com.czy.seed.mybatis.entity.TestEntity;
//import com.czy.seed.mybatis.mapper.SqlServerMapper;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import java.util.List;
//
///**
// * Created by panlc on 2017-03-24.
// */
//public class SqlServerTest {
//
//    static ClassPathXmlApplicationContext ctx;
//    static SqlServerMapper sqlMapper;
//
//    @Ignore
//    public static void beforeClass() {
//        ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:applicationContext.xml"});
//        ctx.start();
//        sqlMapper = ctx.getBean(SqlServerMapper.class);
//    }
//
//    @Ignore
//    public void testInsertAndDeleteByPrimaryKeyAndUpdate() {
//        TestEntity testEntity = new TestEntity();
//        testEntity.setName("tsetest");
//        long insert = sqlMapper.insert(testEntity);
//        Assert.assertTrue(testEntity.getId() != null);
//        int i = sqlMapper.updateByPrimaryKey(testEntity);
//        int delete = sqlMapper.deleteByPrimaryKey(testEntity.getId());
//    }
//
//    @Ignore
//    public void testSelectByPrimaryKey() {
//        TestEntity testEntity = new TestEntity();
//        testEntity.setName("tsetest");
//        long insert = sqlMapper.insert(testEntity);
//        Long id = testEntity.getId();
//        TestEntity testEntity1 = sqlMapper.selectByPrimaryKey(id);
//        Assert.assertTrue(id == testEntity1.getId());
//    }
//
//    @Ignore
//    public void testSelectListByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        queryParams.selectProperties("name");
//        QueryParams.Criteria criteria = queryParams.createCriteria();
////        criteria.andEqualTo("name", "123");
////        criteria.andLike("name", "%et%");
//        criteria.andBetween("id", 1, 13);
////        criteria.andCondition("1=-1");
//        List<TestEntity> testEntities = sqlMapper.selectListByParams(queryParams);
//        System.out.println(testEntities);
//    }
//
//    @Ignore
//    public void testSelectOneByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
////        queryParams.selectProperties("name");
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andEqualTo("id", 2);
//        TestEntity testEntity = sqlMapper.selectOneByParams(queryParams);
//        System.out.println(testEntity.getId());
//    }
//
//    @Ignore
//    public void testDeleteByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andEqualTo("name", "et");
//        int i = sqlMapper.deleteByParams(queryParams);
//        Assert.assertTrue(i == 0);
//    }
//
//    @Ignore
//    public void testUpdateSelectiveByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andEqualTo("name", "et");
//        TestEntity testEntity = new TestEntity();
//        testEntity.setName("tsetest");
//        int i = sqlMapper.updateSelectiveByParams(testEntity, queryParams);
//    }
//
//    @Ignore
//    public void testUpdateByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
////        criteria.andEqualTo("name", "et");
//        TestEntity testEntity = new TestEntity();
//        int i = sqlMapper.updateByParams(testEntity, queryParams);
//    }
//
//    @Ignore
//    public void testSelectCountByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andEqualTo("name", "tsetest");
//        int i = sqlMapper.selectCountByParams(queryParams);
//        System.out.println(i);
//    }
//
//
//    @Ignore
//    public void testSelectListRelativeByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andLike("name", "%et%").andCondition("ONE_one.id = 2");
//        List<TestEntity> testEntities = sqlMapper.selectListRelativeByParams(queryParams);
//        System.out.println(testEntities);
//    }
//
//    @Ignore
//    public void testSelectOneRelativeByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andLike("name", "%et%").andCondition("ONE_oneList.id = 1");
//        TestEntity testEntity = sqlMapper.selectOneRelativeByParams(queryParams);
//        System.out.println(testEntity);
//    }
//
//
//}

//package com.czy.seed.mybatis.test;
//
//import com.czy.seed.mybatis.base.QueryParams;
//import com.czy.seed.mybatis.entity.TestEntity;
//import com.czy.seed.mybatis.mapper.OracleMapper;
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
//public class OracleTest {
//
//    static ClassPathXmlApplicationContext ctx;
//    static OracleMapper oracleMapper;
//
//    @Ignore
//    public static void beforeClass() {
//        ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:applicationContext.xml"});
//        ctx.start();
//        oracleMapper = ctx.getBean(OracleMapper.class);
//    }
//
//    @Ignore
//    public void testInsertAndDeleteByPrimaryKeyAndUpdate() {
//        TestEntity testEntity = new TestEntity();
////        testEntity.setName("tsetest");
//        long insert = oracleMapper.insert(testEntity);
//        Assert.assertTrue(testEntity.getId() != null);
//        int i = oracleMapper.updateByPrimaryKey(testEntity);
//        Assert.assertEquals(1, i);
//        int delete = oracleMapper.deleteByPrimaryKey(testEntity.getId());
//        Assert.assertEquals(1, delete);
//    }
//
//    @Ignore
//    public void testSelectByPrimaryKey() {
//        TestEntity testEntity = new TestEntity();
//        testEntity.setName("tsetest");
//        long insert = oracleMapper.insert(testEntity);
//        Long id = testEntity.getId();
//        TestEntity testEntity1 = oracleMapper.selectByPrimaryKey(id);
//        Assert.assertTrue(id == testEntity1.getId());
//    }
//
//    @Ignore
//    public void testSelectListByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        queryParams.orderBy("id").asc().orderBy("name").desc();
//        queryParams.selectProperties("name");
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andEqualTo("name", "123");
//        criteria.andLike("name", "%et%");
//        criteria.andBetween("id", 10, 13);
////        criteria.andCondition("1=-1");
//        List<TestEntity> testEntities = oracleMapper.selectListByParams(queryParams);
//        System.out.println(testEntities);
//    }
//
//    @Ignore
//    public void testSelectOneByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
////        queryParams.selectProperties("name");
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andEqualTo("id", 27);
//        TestEntity testEntity = oracleMapper.selectOneByParams(queryParams);
//        System.out.println(testEntity);
//    }
//
//    @Ignore
//    public void testDeleteByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
////        criteria.andEqualTo("name", "et");
//        int i = oracleMapper.deleteByParams(queryParams);
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
//        int i = oracleMapper.updateSelectiveByParams(testEntity, null);
//    }
//
//    @Ignore
//    public void testUpdateByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andEqualTo("name", "et");
//        TestEntity testEntity = new TestEntity();
//        int i = oracleMapper.updateByParams(testEntity, queryParams);
//    }
//
//    @Ignore
//    public void testSelectCountByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andEqualTo("name", "tsetest");
//        int i = oracleMapper.selectCountByParams(queryParams);
//        System.out.println(i);
//    }
//
//
//
//    @Ignore
//    public void testSelectRelativeByPrimaryKey() {
//        TestEntity testEntity = oracleMapper.selectRelativeByPrimaryKey(2);
//        System.out.println(testEntity);
//    }
//
//    @Ignore
//    public void testSelectListRelativeByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        queryParams.orderBy("id").asc().orderBy("name").desc();
//        QueryParams.Criteria or = queryParams.or();
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andLike("name", "%et%").andCondition("ONE_one.id = 1");
//        or.andEqualTo("id", 12);
//        List<TestEntity> testEntities = oracleMapper.selectListRelativeByParams(queryParams);
//        System.out.println(testEntities);
//    }
//
//    @Ignore
//    public void testSelectOneRelativeByParams() {
//        QueryParams queryParams = new QueryParams(TestEntity.class);
//        QueryParams.Criteria criteria = queryParams.createCriteria();
//        criteria.andLike("name", "%et%").andCondition("ONE_oneList.id = 3");
////        criteria.andEqualTo("id", 20);
//        TestEntity testEntity = oracleMapper.selectOneRelativeByParams(queryParams);
//        System.out.println(testEntity);
//    }
//
//
//}

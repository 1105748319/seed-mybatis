//package com.czy.seed.mybatis.sql.entity;
//
//import com.czy.seed.mybatis.config.mybatis.annotations.Join;
//import com.czy.seed.mybatis.entity.One;
//import org.junit.Assert;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
///**
// * Created by panlc on 2017-03-29.
// */
//public class MybatisAssociationTest {
//
//    @Ignore
//    public void testNewInstance() {
//        MybatisAssociation one = new MybatisAssociation("testRollback", "one",
//                One.class, "name, id", "id=test_id,name=test_name", Join.LEFT);
//        assertEquals("ONE", one.getTargetTableName());
//        String joinCondition = one.getJoinCondition();
//        Assert.assertEquals("left join testRollback.id = ONE.test_id and testRollback.name = ONE.test_name ", joinCondition);
//        System.out.println(joinCondition);
//    }
//}
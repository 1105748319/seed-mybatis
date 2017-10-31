//package com.czy.seed.mybatis.sql.util;
//
//import com.czy.seed.mybatis.config.mybatis.annotations.One2Many;
//import com.czy.seed.mybatis.config.mybatis.annotations.One2One;
//import com.czy.seed.mybatis.entity.TestEntity;
//import org.junit.Assert;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//
///**
// * Created by panlc on 2017-03-27.
// */
//public class MybatisColumnsOGNLTest {
//
//    @Ignore
//    public void hasValue() throws Exception {
//        TestEntity record = new TestEntity();
//        boolean b = MybatisColumnsOGNL.hasValue(record);
//        Assert.assertTrue(b == false);
//        record.setName("123");
//        boolean c = MybatisColumnsOGNL.hasValue(record);
//        Assert.assertTrue(c);
//    }
//
//    @Ignore
//    public void test() throws NoSuchFieldException {
//        TestEntity t = new TestEntity();
//        Field one = t.getClass().getDeclaredField("one");
//        one.setAccessible(true);
//        boolean annotationPresent = one.isAnnotationPresent(One2Many.class);
////        One2Many annotation = one.getAnnotation(One2Many.class);
//
//        Field one1 = t.getClass().getDeclaredField("one1");
//        one1.setAccessible(true);
//        boolean annotationPresent2 = one.isAnnotationPresent(One2One.class);
//
//        Annotation[] annotations = one.getAnnotations();
//
//        System.out.println(annotations);
//    }
//
//}
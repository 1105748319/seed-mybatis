//package com.czy.seed.mybatis.sql.helper;
//
//import com.czy.seed.mybatis.entity.Many;
//import com.czy.seed.mybatis.entity.One;
//import com.czy.seed.mybatis.entity.TestEntity;
//import org.junit.Ignore;
//import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;
//
//import java.lang.reflect.GenericDeclaration;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//
///**
// * Created by PLC on 2017/4/30.
// */
//public class EntityHelperTest {
//
//    @Ignore
//    public void getEntityClass() throws Exception {
//        Class<?> entityClass = EntityHelper.getEntityClass(B.class);
//        System.out.println(entityClass.getName());
//
//    }
//
//    @Ignore
//    public void test2() {
//        Class<Object> superClassGenricType = getSuperClassGenricType(E.class, 0);
//        System.out.println(superClassGenricType.getName());
//    }
//
//    /**
//     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
//     *
//     * @param clazz clazz The class to introspect
//     * @param index the Index of the generic ddeclaration,start from 0.
//     * @return the index generic declaration, or Object.class if cannot be
//     * determined
//     */
//    public Class<Object> getSuperClassGenricType(final Class clazz, final int index) {
//
//        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
//        Type genType = clazz.getGenericSuperclass();
//        if (!(genType instanceof ParameterizedType)) {
//            return Object.class;
//        }
//        //返回表示此类型实际类型参数的 Type 对象的数组。
//        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
//        if (index >= params.length || index < 0) {
//            return Object.class;
//        }
//        if (params[index] instanceof TypeVariableImpl) {
//            GenericDeclaration genericDeclaration = ((TypeVariableImpl) params[index]).getGenericDeclaration();
//            genericDeclaration.getTypeParameters()[index].getName();
//            return null;
//        }
//        if (!(params[index] instanceof Class)) {
//            return Object.class;
////            return (Class)params[index];
//        }
////        GenericDeclaration genericDeclaration = ((TypeVariableImpl) params[index]).getGenericDeclaration();
////        Class a = (Class) genericDeclaration;
////        System.out.println(a.getName());
//        return (Class) params[index];
//    }
//
//
//    interface A<T> {
//
//    }
//
//    interface B extends A<TestEntity> {
//
//    }
//
//    class C<T, M> {
//    }
//
//    class D<Mane, One> extends C<TestEntity, Many> {
//    }
//
//    class E extends D<One, TestEntity> {
//
//    }
//
//}
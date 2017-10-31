package com.czy.seed.mvc.util;

import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by PLC on 2017/6/10.
 */
public class GenricUtil {

    public static Class getGenericClass(final Class clazz, final int index) {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (params[index] instanceof TypeVariableImpl) {
            GenericDeclaration genericDeclaration = ((TypeVariableImpl) params[index]).getGenericDeclaration();
            genericDeclaration.getTypeParameters()[index].getName();
            return null;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];

    }
}

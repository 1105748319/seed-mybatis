package com.czy.seed.mybatis.sql.entity;

/**
 * 字段转换方式
 * Created by panlc on 2017-03-21.
 */
public enum Style {
    normal,                     //原值
    camelhump,                  //驼峰转下划线
    uppercase,                  //转换为大写
    lowercase,                  //转换为小写
    camelhumpAndUppercase,      //驼峰转下划线大写形式
    camelhumpAndLowercase,      //驼峰转下划线小写形式
}
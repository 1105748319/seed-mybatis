package com.czy.seed.mvc.conf.exception;

/**
 * 自定义异常类
 * Created by 003914[panlc] on 2017-06-08.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

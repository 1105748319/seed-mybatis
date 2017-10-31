package com.czy.seed.mvc.base.exception;

/**
 * Controller名称错误异常
 * Created by 003914[panlc] on 2017-06-06.
 */
public class IllegalleControllerNameException extends RuntimeException {

    public IllegalleControllerNameException() {
        super("Controller名称必须符合ModelNameController规则");
    }

}

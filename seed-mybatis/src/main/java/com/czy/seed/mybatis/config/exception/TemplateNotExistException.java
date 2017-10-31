package com.czy.seed.mybatis.config.exception;

/**
 * Created by panlc on 2017-03-20.
 */
public class TemplateNotExistException extends RuntimeException {
    public TemplateNotExistException() {
        super();
    }

    public TemplateNotExistException(String message) {
        super(message);
    }

    public TemplateNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateNotExistException(Throwable cause) {
        super(cause);
    }
}

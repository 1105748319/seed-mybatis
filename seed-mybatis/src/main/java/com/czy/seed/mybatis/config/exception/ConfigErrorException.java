package com.czy.seed.mybatis.config.exception;

/**
 * Created by PLC on 2017/4/30.
 */
public class ConfigErrorException extends RuntimeException {
    public ConfigErrorException() {
        super();
    }

    public ConfigErrorException(String message) {
        super(message);
    }

    public ConfigErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigErrorException(Throwable cause) {
        super(cause);
    }
}

package com.czy.seed.mybatis.config.exception;

/**
 * Created by panlc on 2017-03-13.
 */
public class DataSourceBuildException extends RuntimeException {

    public DataSourceBuildException() {
        super();
    }

    public DataSourceBuildException(String message) {
        super(message);
    }

    public DataSourceBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceBuildException(Throwable cause) {
        super(cause);
    }
}

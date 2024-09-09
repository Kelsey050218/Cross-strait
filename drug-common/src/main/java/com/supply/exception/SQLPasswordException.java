package com.supply.exception;

public class SQLPasswordException extends RuntimeException{
    public SQLPasswordException() {
        super();
    }

    public SQLPasswordException(String message) {
        super(message);
    }

    public SQLPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLPasswordException(Throwable cause) {
        super(cause);
    }

    protected SQLPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

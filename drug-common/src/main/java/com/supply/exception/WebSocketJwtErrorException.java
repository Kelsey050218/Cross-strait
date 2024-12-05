package com.supply.exception;

public class WebSocketJwtErrorException extends RuntimeException {
    public WebSocketJwtErrorException() {
        super();
    }

    public WebSocketJwtErrorException(String message) {
        super(message);
    }

    public WebSocketJwtErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebSocketJwtErrorException(Throwable cause) {
        super(cause);
    }

    protected WebSocketJwtErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

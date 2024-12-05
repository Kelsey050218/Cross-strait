package com.supply.exception;

public class WebSocketSendException extends RuntimeException{
    public WebSocketSendException() {
        super();
    }

    public WebSocketSendException(String message) {
        super(message);
    }

    public WebSocketSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebSocketSendException(Throwable cause) {
        super(cause);
    }

    protected WebSocketSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

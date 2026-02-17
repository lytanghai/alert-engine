package com.finance.alert_engine.custom.exception.model;

public class UnknownException extends SystemException{
    public UnknownException(String code, String message) {
        super(code, message);
    }

    public UnknownException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}

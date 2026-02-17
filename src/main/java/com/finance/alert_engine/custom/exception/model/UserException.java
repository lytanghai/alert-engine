package com.finance.alert_engine.custom.exception.model;

public class UserException extends SystemException{
    public UserException(String code, String message) {
        super(code, message);
    }

    public UserException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}

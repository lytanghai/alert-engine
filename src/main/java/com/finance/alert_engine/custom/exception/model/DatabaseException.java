package com.finance.alert_engine.custom.exception.model;

public class DatabaseException extends SystemException{
    public DatabaseException(String code, String message) {
        super(code, message);
    }

    public DatabaseException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}

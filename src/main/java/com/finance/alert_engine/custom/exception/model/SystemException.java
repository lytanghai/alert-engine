package com.finance.alert_engine.custom.exception.model;


public class SystemException extends RuntimeException{
    private final String code;
    private final String message;

    public SystemException(String code, String message) {
        super(message); // keep RuntimeException message
        this.code = code;
        this.message = message;
    }

    public SystemException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

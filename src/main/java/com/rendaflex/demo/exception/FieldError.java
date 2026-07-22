package com.rendaflex.demo.exception;

public class FieldError {

    private final String field;
    private final String code;
    private final String message;
    private final Object rejectedValue;

    public FieldError(String field, String code, String message, Object rejectedValue) {
        this.field = field;
        this.code = code;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}

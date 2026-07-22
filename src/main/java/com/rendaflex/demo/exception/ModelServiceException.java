package com.rendaflex.demo.exception;

public class ModelServiceException extends RuntimeException {

    public ModelServiceException(String message) {
        super(message);
    }

    public ModelServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

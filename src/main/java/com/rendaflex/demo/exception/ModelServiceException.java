package com.rendaflex.demo.exception;

public class ModelServiceException extends RuntimeException {

    private final ApiErrorCode code;

    public ModelServiceException(ApiErrorCode code, String message) {
        super(message);
        if (code != ApiErrorCode.MODEL_SERVICE_INVALID_RESPONSE
                && code != ApiErrorCode.MODEL_SERVICE_UNAVAILABLE
                && code != ApiErrorCode.MODEL_SERVICE_TIMEOUT) {
            throw new IllegalArgumentException("Código inválido para falha do serviço de modelo.");
        }
        this.code = code;
    }

    public ApiErrorCode getCode() {
        return code;
    }
}

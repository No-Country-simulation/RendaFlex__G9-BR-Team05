package com.rendaflex.demo.exception;

import java.util.List;

public record ApiError(
        ApiErrorCode code,
        String message,
        List<ApiFieldError> fieldErrors
) {
}

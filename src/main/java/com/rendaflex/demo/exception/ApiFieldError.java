package com.rendaflex.demo.exception;

public record ApiFieldError(
        String field,
        String message
) {
}

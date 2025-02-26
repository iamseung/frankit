package com.example.frankit.exception;

import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {
    private final ProductErrorCode errorCode;
    private final String message;

    public ProductException(ProductErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "[%s] %s".formatted(errorCode, message);
    }
}

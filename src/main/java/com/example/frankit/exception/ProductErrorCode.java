package com.example.frankit.exception;

public enum ProductErrorCode {
    FAIL_PRODUCT_CREATE_REQUEST("상품 생성에 실패했습니다."),
    NOT_EXIST_PRODUCT("상품이 존재하지 않습니다,"),
    FAIL_PRODUCT_OPTION_CREATE_REQUEST("상품 옵션 생성에 실패했습니다."),
    NOT_EXIST_PRODUCT_OPTION("상품 옵션이 존재하지 않습니다,");

    public final String message;

    ProductErrorCode(String message) {
        this.message = message;
    }
}

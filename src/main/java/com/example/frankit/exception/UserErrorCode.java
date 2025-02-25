package com.example.frankit.exception;

public enum UserErrorCode {

    DUPLICATE_USER_ID("중복된 아이디가 입니다."),
    IS_FORBIDDEN_ACCESS("허용되지 않은 접근입니다.");

    public final String message;

    UserErrorCode(String message) {
        this.message = message;
    }

}

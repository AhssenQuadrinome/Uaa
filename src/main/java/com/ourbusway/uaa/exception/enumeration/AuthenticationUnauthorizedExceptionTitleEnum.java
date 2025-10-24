package com.ourbusway.uaa.exception.enumeration;

public enum AuthenticationUnauthorizedExceptionTitleEnum implements BaseExceptionEnum {
    NOT_AUTHORIZED("UAA_AUTH_UNAU_ERR_1"),

    EXPIRED_TOKEN("UAA_AUTH_UNAU_ERR_2");

    private final String code;

    AuthenticationUnauthorizedExceptionTitleEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}

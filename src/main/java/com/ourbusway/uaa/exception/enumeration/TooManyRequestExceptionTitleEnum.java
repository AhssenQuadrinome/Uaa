package com.ourbusway.uaa.exception.enumeration;

public enum TooManyRequestExceptionTitleEnum implements BaseExceptionEnum {
    MAX_LOGIN_ATTEMPT("UAA_TMR_ERR_1");

    private final String code;

    TooManyRequestExceptionTitleEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}

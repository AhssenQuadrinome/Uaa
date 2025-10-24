package com.ourbusway.uaa.exception.enumeration;

public enum MethodArgumentsExceptionTitleEnum implements BaseExceptionEnum {
    METHOD_ARGUMENTS_NOT_VALID("UAA_BAD_REQ_ERR");

    private final String code;

    MethodArgumentsExceptionTitleEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}

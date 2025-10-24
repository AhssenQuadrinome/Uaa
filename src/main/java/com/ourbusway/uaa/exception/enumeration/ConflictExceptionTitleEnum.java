package com.ourbusway.uaa.exception.enumeration;

public enum ConflictExceptionTitleEnum implements BaseExceptionEnum {
    PASSWORD_MISMATCH("UAA_CFT_ERR_1");
    private final String code;

    ConflictExceptionTitleEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}

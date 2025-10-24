package com.ourbusway.uaa.exception.enumeration;

public enum ResourceNotFoundExceptionTitleEnum implements BaseExceptionEnum {

    USER_NOT_FOUND("UAA_RNF_ERR_1"),

    RESET_KEY_NOT_FOUND("UAA_RNF_ERR_3"),

    ACTIVATION_KEY_NOT_FOUND("UUA_RNF_ERR_4"),

    RECENT_DEVICE_NOT_FOUND("UAA_RNF_ERR_5");


    private final String code;

    ResourceNotFoundExceptionTitleEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}

package com.ourbusway.uaa.exception.enumeration;

public enum AuthorizationForbiddenExceptionTitleEnum implements BaseExceptionEnum {
    ACTIVATION_KEY_EXPIRED("UAA_AUTH_FORB_ERR_1"),

    FORBIDDEN("UAA_AUTH_FORB_ERR_2"),

    TOKEN_NOT_VALID("UAA_AUTH_FORB_ERR_3"),

    USER_NOT_ACTIVATED("UAA_AUTH_FORB_ERR_4"),

    RESET_KEY_EXPIRED("UAA_AUTH_FORB_ERR_5"),

    USER_DISABLED("UAA_AUTH_FORB_ERR_6");

    private final String code;


    AuthorizationForbiddenExceptionTitleEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}

package com.ourbusway.uaa.exception;

import com.ourbusway.uaa.exception.enumeration.AuthorizationForbiddenExceptionTitleEnum;
import org.springframework.http.HttpStatus;

public class AuthorizationForbiddenException extends AbstractBaseException {
    public AuthorizationForbiddenException(
            AuthorizationForbiddenExceptionTitleEnum title, String message) {
        super(title, HttpStatus.FORBIDDEN, message);
    }
}

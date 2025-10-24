package com.ourbusway.uaa.exception;

import com.ourbusway.uaa.exception.enumeration.BaseExceptionEnum;
import org.springframework.http.HttpStatus;

public class ConflictException extends AbstractBaseException {

    public ConflictException(BaseExceptionEnum title, String message) {
        super(title, HttpStatus.UNAUTHORIZED, message);
    }
}

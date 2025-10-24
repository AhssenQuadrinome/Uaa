package com.ourbusway.uaa.exception;

import com.ourbusway.uaa.exception.enumeration.TooManyRequestExceptionTitleEnum;
import org.springframework.http.HttpStatus;

public class TooManyRequestException extends AbstractBaseException {

    public TooManyRequestException(TooManyRequestExceptionTitleEnum title, String message) {
        super(title, HttpStatus.TOO_MANY_REQUESTS, message);
    }
}

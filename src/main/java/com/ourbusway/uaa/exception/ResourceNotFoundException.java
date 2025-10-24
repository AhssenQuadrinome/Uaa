package com.ourbusway.uaa.exception;

import com.ourbusway.uaa.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractBaseException {
    public ResourceNotFoundException(ResourceNotFoundExceptionTitleEnum title, String message) {
        super(title, HttpStatus.NOT_FOUND, message);
    }
}

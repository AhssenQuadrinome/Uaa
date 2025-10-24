package com.ourbusway.uaa.controller;

import com.ourbusway.uaa.exception.AbstractBaseException;
import com.ourbusway.uaa.resource.error.ErrorDetailsResource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface ErrorController {

    ResponseEntity<ErrorDetailsResource> handleGenericExceptions(
            AbstractBaseException e, HttpServletRequest request);

    ResponseEntity<ErrorDetailsResource> handleValidationError(
            MethodArgumentNotValidException e, HttpServletRequest request);
}

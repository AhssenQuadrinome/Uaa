package com.ourbusway.uaa.controller.impl;

import com.ourbusway.uaa.controller.ErrorController;
import com.ourbusway.uaa.exception.*;
import com.ourbusway.uaa.exception.enumeration.MethodArgumentsExceptionTitleEnum;
import com.ourbusway.uaa.resource.error.ErrorDetailsResource;
import com.ourbusway.uaa.resource.error.ValidationErrorResource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ErrorControllerImpl implements ErrorController {


    @Override
    @ExceptionHandler({
            AuthenticationUnauthorizedException.class,
            AuthorizationForbiddenException.class,
            TooManyRequestException.class,
            ResourceNotFoundException.class
    })
    public ResponseEntity<ErrorDetailsResource> handleGenericExceptions(
            AbstractBaseException e, HttpServletRequest request) {
        ErrorDetailsResource errorDetailResource = new ErrorDetailsResource();
        errorDetailResource.setTimestamp(Instant.now().toEpochMilli());
        errorDetailResource.setTitle(e.getTitle().toString());
        errorDetailResource.setCode(e.getTitle().getCode());
        errorDetailResource.setDeveloperMessage(e.getClass().getName());
        errorDetailResource.setStatus(e.getStatus().value());
        errorDetailResource.setDetail(e.getMessage());
        return new ResponseEntity<>(errorDetailResource, e.getStatus());
    }

    @Override
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailsResource> handleValidationError(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorDetailsResource errorDetailResource = new ErrorDetailsResource();
        errorDetailResource.setTimestamp(Instant.now().toEpochMilli());
        errorDetailResource.setTitle(
                MethodArgumentsExceptionTitleEnum.METHOD_ARGUMENTS_NOT_VALID.toString());
        errorDetailResource.setCode(
                MethodArgumentsExceptionTitleEnum.METHOD_ARGUMENTS_NOT_VALID.getCode());
        errorDetailResource.setDeveloperMessage(e.getClass().getName());
        errorDetailResource.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetailResource.setDetail("Input validation failed");
        // Create ValidationError instances
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {
            List<ValidationErrorResource> validationErrorList =
                    errorDetailResource.getErrors().computeIfAbsent(fe.getField(), k -> new ArrayList<>());
            ValidationErrorResource validationError = new ValidationErrorResource();
            validationError.setCode(fe.getCode());
            validationError.setMessage(fe.getDefaultMessage());
            validationErrorList.add(validationError);
        }
        return new ResponseEntity<>(errorDetailResource, HttpStatus.BAD_REQUEST);
    }
}

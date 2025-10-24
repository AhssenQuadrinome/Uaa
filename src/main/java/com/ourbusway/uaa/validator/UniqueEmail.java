package com.ourbusway.uaa.validator;

import com.ourbusway.uaa.validator.impl.UniqueEmailImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailImpl.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

    String message() default "An account with this email already exists.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

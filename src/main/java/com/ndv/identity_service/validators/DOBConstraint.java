package com.ndv.identity_service.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DOBValidator.class})
public @interface DOBConstraint {

    String message() default "{jakarta.validation.constraints.Size.message}";

    int min();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

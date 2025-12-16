package com.pklinh.student_management.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Constraint(validatedBy = {DobValidator.class})
@Documented
@Retention(RUNTIME)

public @interface DobConstraint {
    String message() default "Invalid date of birth";

    int min();
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

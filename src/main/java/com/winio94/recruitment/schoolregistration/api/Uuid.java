package com.winio94.recruitment.schoolregistration.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})", message = "must be valid UUID format")
@Constraint(validatedBy = {})
public @interface Uuid {

    String message() default "must be valid UUID format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

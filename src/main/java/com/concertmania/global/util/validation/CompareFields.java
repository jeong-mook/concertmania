package com.concertmania.global.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompareFieldsValidator.class)
@Documented
public @interface CompareFields {
    String message() default "{com.concertmania.common.validation.CompareFields.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String baseField();
    String matchField();

    /**
     * true면 baseField < matchField
     * false면 baseField <= matchField
     */
    boolean lessThan() default true;
}
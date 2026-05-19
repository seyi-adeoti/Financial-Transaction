package com.oluwaseyi.in.Moneymanager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface ValidCurrency {

    String message() default "Currency must be one of: USD, EUR, GBP, NGN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.oluwaseyi.in.Moneymanager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    private static final Set<String> ALLOWED_CURRENCIES = Set.of("USD", "EUR", "GBP", "NGN");

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext context) {
        if (currency == null || currency.isBlank()) {
            return false;
        }
        return ALLOWED_CURRENCIES.contains(currency.trim().toUpperCase());
    }
}

package com.mertalptekin.springbootrestapp.application.product.create.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class NotReservedProductNameValidator implements ConstraintValidator<NotReservedProductName, String> {

    private static final Set<String> RESERVED_NAMES = Set.of("test", "deneme", "asdf", "sample", "örnek");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null kontrolü @NotNull/@NotBlank'ın sorumluluğunda
        }
        return !RESERVED_NAMES.contains(value.trim().toLowerCase());
    }
}

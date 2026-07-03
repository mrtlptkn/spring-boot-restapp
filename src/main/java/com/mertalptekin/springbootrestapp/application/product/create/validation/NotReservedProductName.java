package com.mertalptekin.springbootrestapp.application.product.create.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Standart Bean Validation anotasyonlarının karşılayamadığı bir iş kuralı:
// ürün adı, yasaklı/placeholder bir kelime listesinden biri olmamalı (test, deneme, asdf vb.).
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotReservedProductNameValidator.class)
public @interface NotReservedProductName {

    String message() default "Product name must not be a reserved/placeholder value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

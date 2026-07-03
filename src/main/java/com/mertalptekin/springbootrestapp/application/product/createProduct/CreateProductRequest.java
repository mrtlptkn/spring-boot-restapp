package com.mertalptekin.springbootrestapp.application.product.createProduct;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mertalptekin.springbootrestapp.application.product.IProductRequest;
import com.mertalptekin.springbootrestapp.application.product.createProduct.validation.NotReservedProductName;
import jakarta.validation.constraints.*;


import java.math.BigDecimal;

// Requestler immutable (değiştirilemez) olmalıdır. Bu nedenle record kullanıyoruz.

public record CreateProductRequest(
        @JsonProperty("productName")
        @NotBlank(message = "Product name must not be blank")
        @NotNull(message = "Product name must not be null")
        @NotEmpty(message = "Product name must not be empty")
        @Size(max = 20, message = "Product name must not exceed 20 characters")
        @NotReservedProductName(message = "Product name must not be a reserved/placeholder value (e.g. 'test', 'deneme')")
        String name,
        @JsonProperty("unitPrice")
        @Positive(message = "Price must be positive")
        @NotNull(message = "Price must not be null")
        @Min(value = 1, message = "Price must be at least 0")
        BigDecimal price,
        @JsonProperty("unitsInStock")
        @NotNull(message = "Stock must not be null")
        @Positive(message = "Stock must be positive")
        @Min(value = 1, message = "Stock must be at least 0")
        @Max(value = 10, message = "Stock must not exceed 10")
        int stock) implements IProductRequest {
}

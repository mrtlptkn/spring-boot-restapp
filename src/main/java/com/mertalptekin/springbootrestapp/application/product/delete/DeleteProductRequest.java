package com.mertalptekin.springbootrestapp.application.product.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mertalptekin.springbootrestapp.application.product.IProductRequest;

public record DeleteProductRequest(
        @JsonProperty("productId")
        Integer id) implements IProductRequest {
}

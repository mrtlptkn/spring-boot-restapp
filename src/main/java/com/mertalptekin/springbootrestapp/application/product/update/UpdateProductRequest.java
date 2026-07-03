package com.mertalptekin.springbootrestapp.application.product.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mertalptekin.springbootrestapp.application.product.IProductRequest;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @JsonProperty("productId")
        Integer id,
        @JsonProperty("productName")
        String name,
        @JsonProperty("unitPrice")
        BigDecimal price,
        @JsonProperty("unitsInStock")
        int stock
)  implements IProductRequest {

}

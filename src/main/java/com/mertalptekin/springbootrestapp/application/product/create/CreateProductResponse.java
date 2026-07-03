package com.mertalptekin.springbootrestapp.application.product.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mertalptekin.springbootrestapp.application.product.IProductResponse;
import lombok.Data;

import java.math.BigDecimal;

// Response sınıfları mutable (değiştirilebilir) olabilir. Bu nedenle class kullanıyoruz.

@Data
public final class CreateProductResponse implements IProductResponse {
        @JsonProperty("productId")
        private Integer id;
        @JsonProperty("productName")
        private String name;
        @JsonProperty("unitPrice")
        private BigDecimal price;
        @JsonProperty("unitsInStock")
        private Integer stock;
}

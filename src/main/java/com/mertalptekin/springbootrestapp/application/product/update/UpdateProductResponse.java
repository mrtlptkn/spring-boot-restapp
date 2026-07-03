package com.mertalptekin.springbootrestapp.application.product.update;

import com.mertalptekin.springbootrestapp.application.product.IProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateProductResponse implements IProductResponse {
    private String message;
}

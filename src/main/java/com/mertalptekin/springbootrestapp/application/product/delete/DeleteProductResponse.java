package com.mertalptekin.springbootrestapp.application.product.delete;

import com.mertalptekin.springbootrestapp.application.product.IProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteProductResponse implements IProductResponse {
    private String message;
}

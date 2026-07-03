package com.mertalptekin.springbootrestapp.application.product.delete;

import com.mertalptekin.springbootrestapp.application.product.IProductRequestHandler;
import com.mertalptekin.springbootrestapp.domain.entity.Product;
import com.mertalptekin.springbootrestapp.domain.service.product.IProductService;
import org.springframework.stereotype.Component;

@Component
public class DeleteProductRequestHandler implements IProductRequestHandler<DeleteProductRequest,DeleteProductResponse> {
    private IProductService productService;

    public DeleteProductRequestHandler(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public DeleteProductResponse handle(DeleteProductRequest request) {

        Product entity =  this.productService.getProductById(request.id()); // Check if product exists
        this.productService.deleteProduct(request.id());

        return new DeleteProductResponse(entity.getName() + " Product deleted successfully");
    }
}

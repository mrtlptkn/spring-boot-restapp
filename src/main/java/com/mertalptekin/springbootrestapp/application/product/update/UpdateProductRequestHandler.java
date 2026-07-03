package com.mertalptekin.springbootrestapp.application.product.update;
import com.mertalptekin.springbootrestapp.application.product.IProductRequestHandler;
import com.mertalptekin.springbootrestapp.domain.entity.Product;
import com.mertalptekin.springbootrestapp.domain.service.product.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UpdateProductRequestHandler implements IProductRequestHandler<UpdateProductRequest, UpdateProductResponse> {

    private final IProductService productService;

    public UpdateProductRequestHandler(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public UpdateProductResponse handle(UpdateProductRequest request) {
        // Implementation for updating the product goes here

        Product entity = this.productService.getProductById(request.id()); // Check if product exists
        BeanUtils.copyProperties(request, entity);

        productService.updateProduct(entity);

        return new UpdateProductResponse("Product updated successfully");
    }
}

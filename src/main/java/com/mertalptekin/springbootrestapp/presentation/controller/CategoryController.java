package com.mertalptekin.springbootrestapp.presentation.controller;

import com.mertalptekin.springbootrestapp.application.category.CategoryResponseDto;
import com.mertalptekin.springbootrestapp.application.category.ProductResponseDto;
import com.mertalptekin.springbootrestapp.domain.entity.Category;
import com.mertalptekin.springbootrestapp.domain.entity.Product;
import com.mertalptekin.springbootrestapp.infra.repository.ICategoryRepository;
import com.mertalptekin.springbootrestapp.infra.repository.IProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;
    private final ModelMapper modelMapper; // Genel olarak bu işlemler Application Layerda yazılır.

    public CategoryController(ICategoryRepository categoryRepository, IProductRepository productRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    // api/categories?id=5

    @GetMapping
    public ResponseEntity<String> getCategories(@RequestParam(required = false) Integer id) {

       Optional<Category> category = categoryRepository.findById(id);

       // eğer üzerinde çalıştığım nesnede sadece kategoriye ait alanlanlar üzerinde çalışacak isek bu durumda lazy fetch kullanabiliriz. Daha performanslı olur.
        // ama kategori ile birlikte kategoriye ait ürünler üzerinde de çalışacak isek bu durumda eager fetch kullanmak daha mantıklı olur.

       if(category.isPresent()) {
           return ResponseEntity.ok("Category Name: " + category.get().getProducts().get(0).getName());
       } else {
           return ResponseEntity.notFound().build();

       }
    }

    @GetMapping("/withProducts")
    public ResponseEntity<Category> getCategoriesWithProducts(@RequestParam(required = false) Integer id) {
         Optional<Category> category = categoryRepository.findWithProductsByCategoryId(id);

         if(category.isPresent()) {
              return ResponseEntity.ok(category.get());
         } else {
              return ResponseEntity.notFound().build();
         }
    }

    @GetMapping("/withProductsDtoVersion")
    public ResponseEntity<CategoryResponseDto> getCategoriesWithProductsDto(@RequestParam(required = false) Integer id) {

        Optional<Category> entity = categoryRepository.findWithProductsByCategoryId(id);
        if(entity.isPresent()) {
            CategoryResponseDto responseDto = modelMapper.map(entity.get(), CategoryResponseDto.class);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // api/categories/5 -> DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    // Products Entity

    @GetMapping("/findProductBetweenPrices")
    public ResponseEntity<List<Product>> findProductBetweenPrices(@RequestParam Integer min,@RequestParam Integer max) {
        List<Product> products = productRepository.findByPriceBetween(BigDecimal.valueOf(min), BigDecimal.valueOf(max));

        return ResponseEntity.ok(products);

    }

    @GetMapping("/findProductBetweenPricesDtoVersion")
    public ResponseEntity<List<ProductResponseDto>> findProductBetweenPricesDto(@RequestParam Integer min, @RequestParam Integer max) {
        List<ProductResponseDto> products = productRepository.findByPriceBetween(BigDecimal.valueOf(min), BigDecimal.valueOf(max)).stream().map(product -> modelMapper.map(product, ProductResponseDto.class)).toList();

        return ResponseEntity.ok(products);

    }


    @GetMapping("/paginationAndSorting")
    public ResponseEntity<Page<Product>> findProductBetweenPricesWithPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("price").ascending().and(Sort.by("name").descending()));
        Page<Product> pageModel = productRepository.findAll(
                    pageable);

        System.out.println("totalPages" + pageModel.getTotalPages());
        System.out.println("totalElements" + pageModel.getTotalElements());
        System.out.println("size" + pageModel.getSize());

        return ResponseEntity.ok(pageModel);
    }

}

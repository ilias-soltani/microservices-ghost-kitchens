package com.programmingtechie.productservice.controller;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import com.programmingtechie.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getValidatedProducts();
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String productId) {
        productService.deleteProductById(productId);
    }
    @PatchMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable String productId, @RequestBody ProductRequest productRequest) {
        productService.updateProduct(productId, productRequest);
    }

    @GetMapping("/searchByIdCategory")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProductsByIdCategory(@RequestParam String idCategory) {
        return productService.getValidatedProductsByIdCategory(idCategory);
    }

    @GetMapping("/unvalidated")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getUnvalidatedProducts() {
        return productService.getUnvalidatedProducts();
    }
    @PutMapping("/{productId}/validate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateProduct(@PathVariable String productId) {
        productService.validateProduct(productId);
    }
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProductsByName(@RequestParam String name) {
        return productService.searchProductsByNameIgnoreCaseContaining(name);
    }





}
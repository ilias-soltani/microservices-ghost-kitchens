package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .promoPrice(productRequest.getPromoPrice())
                .minTime(productRequest.getMinTime())
                .maxTime(productRequest.getMaxTime())
                .minQuantity(productRequest.getMinQuantity())
                .maxQuantity(productRequest.getMaxQuantity())
                .img(productRequest.getImg())
                .calories(productRequest.getCalories())
                .availability(productRequest.isAvailability())
                .idCategory(productRequest.getIdCategory())
                .validated(productRequest.isValidated())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        return mapToProductResponse(product);
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .promoPrice(product.getPromoPrice())
                .minTime(product.getMinTime())
                .maxTime(product.getMaxTime())
                .minQuantity(product.getMinQuantity())
                .maxQuantity(product.getMaxQuantity())
                .img(product.getImg())
                .calories(product.getCalories())
                .availability(product.isAvailability())
                .idCategory(product.getIdCategory())
                .validated(product.isValidated())
                .build();
    }
    public void deleteProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        productRepository.delete(product);
        log.info("Product {} is deleted", productId);
    }
    public void updateProduct(String productId, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setPromoPrice(productRequest.getPromoPrice());
        existingProduct.setMinTime(productRequest.getMinTime());
        existingProduct.setMaxTime(productRequest.getMaxTime());
        existingProduct.setMinQuantity(productRequest.getMinQuantity());
        existingProduct.setMaxQuantity(productRequest.getMaxQuantity());
        existingProduct.setImg(productRequest.getImg());
        existingProduct.setCalories(productRequest.getCalories());
        existingProduct.setAvailability(productRequest.isAvailability());
        existingProduct.setIdCategory(productRequest.getIdCategory());
        existingProduct.setValidated(false);


        productRepository.save(existingProduct);
        log.info("Product {} is updated", productId);
    }
    public List<ProductResponse> getValidatedProducts() {
        List<Product> products = productRepository.findByValidated(true); // Query for validated products
        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<ProductResponse> getUnvalidatedProducts() {
        List<Product> unvalidatedProducts = productRepository.findByValidated(false);
        return unvalidatedProducts.stream().map(this::mapToProductResponse).toList();
    }

    public void validateProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        product.setValidated(true);
        productRepository.save(product);
        log.info("Product {} is validated", productId);
    }
    public List<ProductResponse> getValidatedProductsByIdCategory(String idCategory) {
        List<Product> products = productRepository.findByValidatedAndIdCategory(true, idCategory);
        return products.stream().map(this::mapToProductResponse).toList();
    }
    public List<ProductResponse> searchProductsByNameIgnoreCaseContaining(String name) {
        List<Product> products = productRepository.findByNameIgnoreCaseContainingAndValidated(name, true); // Query for validated products by name ignoring case
        return products.stream().map(this::mapToProductResponse).toList();
    }

}
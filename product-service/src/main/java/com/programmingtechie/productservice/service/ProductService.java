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

        // Update fields if they are not null in the request
        if (productRequest.getName() != null) {
            existingProduct.setName(productRequest.getName());
        }
        if (productRequest.getDescription() != null) {
            existingProduct.setDescription(productRequest.getDescription());
        }
        // Similarly update other fields as needed...

        productRepository.save(existingProduct);
        log.info("Product {} is updated", productId);
    }
    public List<ProductResponse> getProductsByIdCategory(String idCategory) {
        List<Product> products = productRepository.findByIdCategory(idCategory);
        return products.stream().map(this::mapToProductResponse).toList();
    }


}

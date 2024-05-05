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
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
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
                .build();
    }
}

package com.programmingtechie.productservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.productservice.authntication.AuthProxy;
import com.programmingtechie.productservice.authntication.VerifyTokenRequest;
import com.programmingtechie.productservice.authntication.VerifyTokenResponse;
import com.programmingtechie.productservice.dto.ProductAvailabilityRequest;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import com.programmingtechie.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    AuthProxy authProxy;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        productService.createProduct(productRequest,userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getValidatedProducts();
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAll(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef","admin"}));
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String productId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef","admin"}));
        productService.deleteProductById(productId);
    }
    @PatchMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable String productId, @RequestBody ProductRequest productRequest,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        productService.updateProduct(productId, productRequest);
    }

    @GetMapping("/searchByIdCategory")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProductsByIdCategory(@RequestParam String idCategory) {
        return productService.getValidatedProductsByIdCategory(idCategory);
    }

    @GetMapping("/searchByIdChef")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProductsByIdChef(@RequestParam String idChef,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        return productService.getProductsByIdChef(idChef);
    }

    @GetMapping("/unvalidated")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getUnvalidatedProducts(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"admin"}));
        return productService.getUnvalidatedProducts();
    }
    @PutMapping("/{productId}/validate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateProduct(@PathVariable String productId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"admin"}));
        productService.validateProduct(productId);
    }
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProductsByName(@RequestParam String name) {
        return productService.searchProductsByNameIgnoreCaseContaining(name);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProductsByWilaya(@RequestParam String wilaya) {
        return productService.searchProductsByWilayaAndValide(wilaya);
    }

    @PutMapping("/availability")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProductAvailability(@RequestBody ProductAvailabilityRequest request,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        productService.updateProductAvailability(request.getProductId(), request.isAvailability());
    }





}
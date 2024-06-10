package com.example.orderservice.controller;

import java.util.Optional;


import com.example.orderservice.DTO.AddProductRequest;
import com.example.orderservice.DTO.UpdateProductRequest;
import com.example.orderservice.authntication.AuthProxy;
import com.example.orderservice.authntication.VerifyTokenRequest;
import com.example.orderservice.authntication.VerifyTokenResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.orderservice.entities.Cart;
import com.example.orderservice.entities.Product;
import com.example.orderservice.service.CartService;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    AuthProxy authProxy;

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCartByUserId(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        Optional<Cart> cart = cartService.getCartByUserId(userId);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductRequest addProductRequest,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        try {
            cartService.addProductToCart(userId, addProductRequest.getProductId(), addProductRequest.getQuantity());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProductInCart(@PathVariable String productId, @RequestBody UpdateProductRequest updateProductRequest,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        try {
            cartService.updateProductInCart(userId, productId, updateProductRequest.getQuantity());
            return ResponseEntity.ok(cartService.getCartByUserId(userId).get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Cart> removeProductFromCart( @PathVariable String productId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(cartService.getCartByUserId(userId).get());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCart(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        cartService.deleteCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout/{adresse}")
    public ResponseEntity<Void> checkout(@PathVariable  String adresse,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        cartService.checkout(userId,adresse);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> removeProductFromAllCarts(@PathVariable String productId) {
        cartService.removeProductFromAllCarts(productId);
        return ResponseEntity.noContent().build();
    }
}

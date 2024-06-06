package com.example.orderservice.controller;

import java.util.Optional;


import com.example.orderservice.authntication.AuthProxy;
import com.example.orderservice.authntication.VerifyTokenRequest;
import com.example.orderservice.authntication.VerifyTokenResponse;
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

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable String userId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        Optional<Cart> cart = cartService.getCartByUserId(userId);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/products")
    public ResponseEntity<Cart> addProductToCart(@PathVariable String userId, @RequestBody Product product,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        cartService.addProductToCart(userId, product);
        return ResponseEntity.ok(cartService.getCartByUserId(userId).get());
    }

    @PutMapping("/{userId}/products")
    public ResponseEntity<?> updateProductInCart(@PathVariable String userId, @RequestBody Product product,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        try {
            cartService.updateProductInCart(userId, product);
            return ResponseEntity.ok(cartService.getCartByUserId(userId).get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/products/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable String userId, @PathVariable String productId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(cartService.getCartByUserId(userId).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteCart(@PathVariable String userId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        cartService.deleteCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<Void> checkout(@PathVariable String userId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        cartService.checkout(userId);
        return ResponseEntity.ok().build();
    }
}

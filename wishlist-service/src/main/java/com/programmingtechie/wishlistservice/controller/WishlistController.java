package com.programmingtechie.wishlistservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.wishlistservice.authntication.AuthProxy;
import com.programmingtechie.wishlistservice.authntication.VerifyTokenRequest;
import com.programmingtechie.wishlistservice.authntication.VerifyTokenResponse;
import com.programmingtechie.wishlistservice.model.Wishlist;
import com.programmingtechie.wishlistservice.model.WishlistItem;
import com.programmingtechie.wishlistservice.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    @Autowired
    AuthProxy authProxy;
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Wishlist> getWishlist(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        return wishlistService.getWishlist(userId);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Wishlist addItemToWishlist( @RequestParam String productId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        return wishlistService.addItemToWishlist(userId, productId);
    }

    @DeleteMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Wishlist removeItemFromWishlist( @PathVariable String productId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        return wishlistService.removeItemFromWishlist(userId, productId);
    }

    @DeleteMapping("/product/{productId}")
    public void removeProductFromAllWishlists(@PathVariable String productId) {
        wishlistService.removeProductFromAllWishlists(productId);
    }


}

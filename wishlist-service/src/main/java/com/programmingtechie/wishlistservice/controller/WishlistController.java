package com.programmingtechie.wishlistservice.controller;

import com.programmingtechie.wishlistservice.model.Wishlist;
import com.programmingtechie.wishlistservice.model.WishlistItem;
import com.programmingtechie.wishlistservice.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Wishlist> getWishlist(@PathVariable String userId) {
        return wishlistService.getWishlist(userId);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Wishlist addItemToWishlist(@RequestParam String userId, @RequestParam String productId) {
        return wishlistService.addItemToWishlist(userId, productId);
    }

    @DeleteMapping("/items/{userId}/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Wishlist removeItemFromWishlist(@PathVariable String userId, @PathVariable String productId) {
        return wishlistService.removeItemFromWishlist(userId, productId);
    }


}

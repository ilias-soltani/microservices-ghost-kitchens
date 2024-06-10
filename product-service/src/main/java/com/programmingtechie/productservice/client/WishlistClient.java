package com.programmingtechie.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "wishlist-service")
public interface WishlistClient {

    @DeleteMapping("/api/wishlist/product/{productId}")
    void removeProductFromAllWishlists(@PathVariable String productId);
}


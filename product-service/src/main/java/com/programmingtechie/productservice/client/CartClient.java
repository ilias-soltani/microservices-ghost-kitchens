package com.programmingtechie.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface CartClient {

    @DeleteMapping("/api/carts/product/{productId}")
    void removeProductFromAllCarts(@PathVariable String productId);
}


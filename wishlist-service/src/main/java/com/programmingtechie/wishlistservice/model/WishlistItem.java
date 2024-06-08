package com.programmingtechie.wishlistservice.model;

import com.programmingtechie.wishlistservice.client.ProductClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItem {
    private String productId;
    private String name;
    private String imageUrl;
    private String description;
    private BigDecimal price;
}

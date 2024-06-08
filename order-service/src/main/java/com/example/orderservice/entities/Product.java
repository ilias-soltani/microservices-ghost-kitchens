package com.example.orderservice.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;
    private String idChef;
    private String imageUrl;

    public BigDecimal calculateTotalPrice() {
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}
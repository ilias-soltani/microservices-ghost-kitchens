package com.example.orderservice.DTO;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {
    private String id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;
    private String idChef;
    private String imageUrl;
}

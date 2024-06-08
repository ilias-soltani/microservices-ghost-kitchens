package com.example.orderservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductRequest {
    private String productId;
    private int quantity;
}

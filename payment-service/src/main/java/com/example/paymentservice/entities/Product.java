package com.example.paymentservice.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Product {
    private String id;
    private String name;
    private double unitPrice;
    private int quantity;
    private double totalPrice;
    private String chefId;
}
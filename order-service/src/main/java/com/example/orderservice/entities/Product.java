package com.example.orderservice.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    private String id;
    private String name;
    private double unitPrice;
    private int quantity;
    private double totalPrice;
    private String chefId;

    public double calculateTotalPrice() {
        return this.unitPrice * this.quantity;
    }
}
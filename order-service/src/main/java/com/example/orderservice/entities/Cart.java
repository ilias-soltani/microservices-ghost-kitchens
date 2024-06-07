package com.example.orderservice.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;

    @NotNull(message = "User is required")
    private String userId;

    @NotNull(message = "Products are required")
    private List<@NotNull Product> products = new ArrayList<>();

    private boolean paid;

    public Cart(String userId) {
        this.userId = userId;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void updateProduct(Product product) {
        for (Product p : products) {
            if (p.getId().equals(product.getId())) {
                p.setQuantity(product.getQuantity());
                p.setUnitPrice(product.getUnitPrice());
                p.setTotalPrice(product.getQuantity() * product.getUnitPrice());
                break;
            }
        }
    }

    public void removeProduct(String productId) {
        products.removeIf(p -> p.getId().equals(productId));
    }
}
package com.example.paymentservice.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import com.example.paymentservice.DTO.ProductDTO;
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
@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Products are required")
    private List<@NotNull ProductDTO> products;

    private double totalPrice;
    private LocalDateTime createdAt;
    private boolean isGrouped;  // pour afficher la commande pour le client

    @NotNull(message = "Chef ID is required")
    private String chefId;
    private String status = "invalid";
}

package com.example.paymentservice.entities;

import java.math.BigDecimal;
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

    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private boolean isGrouped;  // Indicates grouped order

    @NotNull(message = "Chef ID is required")
    private String chefId;
    private String statusChef = "invalid";
    private String agenceId;
    private String statusAgence;

    private String statusOfOrder;

    private String adresse;
    private String imageUrl;

}

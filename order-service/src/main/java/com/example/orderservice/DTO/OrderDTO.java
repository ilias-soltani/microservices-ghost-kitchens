package com.example.orderservice.DTO;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDTO {
    private String id;
    private String userId;

    @NotNull(message = "Products are required")
    private List<@NotNull ProductDTO> products;

    private BigDecimal totalPrice;
    private boolean isGrouped;
    private String chefId;
    private String statusChef;

    private String adresse;

    private String imageUrl;

}

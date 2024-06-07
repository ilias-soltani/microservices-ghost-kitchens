package com.example.paymentservice.DTO;


import java.util.List;
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
public class OrderDTO {
    private String id;
    private String userId;

    @NotNull(message = "Products are required")
    private List<@NotNull ProductDTO> products;

    private double totalPrice;
    private boolean isGrouped;
    private String chefId;
}
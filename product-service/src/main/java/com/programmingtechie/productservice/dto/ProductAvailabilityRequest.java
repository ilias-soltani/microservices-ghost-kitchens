package com.programmingtechie.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAvailabilityRequest {
    private String productId;
    private boolean availability;
}

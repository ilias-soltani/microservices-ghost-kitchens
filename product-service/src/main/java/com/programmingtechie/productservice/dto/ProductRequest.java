package com.programmingtechie.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal promoPrice;
    private String minTime;
    private String maxTime;
    private BigInteger minQuantity;
    private BigInteger maxQuantity;
    private BigInteger calories;
    private boolean availability= true;
    private String idCategory;
    private boolean validated= false;
    private String wilaya;
    private String imageUrl;
}
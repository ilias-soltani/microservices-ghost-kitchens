package com.programmingtechie.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.BigInteger;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal promoPrice;
    private String minTime;
    private String maxTime;
    private BigInteger minQuantity;
    private BigInteger maxQuantity;
    private BigInteger calories;
    private boolean availability;
    private String idCategory;
    private boolean validated;
    private String idChef;
    private String imageUrl;
    private String wilaya;





}
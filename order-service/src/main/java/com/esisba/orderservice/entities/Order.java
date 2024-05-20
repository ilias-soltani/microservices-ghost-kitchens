package com.esisba.orderservice.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.esisba.orderservice.OrderStatusConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotNull(message = "User is required")
    private User user;

    @Pattern(regexp = OrderStatusConstants.PLACED + "|" + OrderStatusConstants.PROCESSING + "|"
            + OrderStatusConstants.SHIPPED + "|" + OrderStatusConstants.DELIVERED + "|"
            + OrderStatusConstants.CANCELLED)
    @NotBlank(message = "Please specify a valid status")
    private String status;

    @NotNull(message = "Products are required")
    private List<@NotNull Product> products;

    private double totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

package com.example.orderservice.client;

import com.example.orderservice.DTO.OrderDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/orders")
    ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO);

    @GetMapping("/api/orders/user/{userId}/grouped")
    ResponseEntity<List<OrderDTO>> getGroupedOrdersByUserId(@PathVariable String userId);

    @PutMapping("/api/orders/{orderId}")
    ResponseEntity<OrderDTO> updateOrder(@PathVariable String orderId, @RequestBody OrderDTO order) ;

}

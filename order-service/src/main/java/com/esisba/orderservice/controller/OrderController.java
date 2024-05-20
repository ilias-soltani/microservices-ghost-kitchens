package com.esisba.orderservice.controller;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esisba.orderservice.entities.Cart;
import com.esisba.orderservice.entities.Order;
import com.esisba.orderservice.service.CartService;
import com.esisba.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Add order to cart
        Cart cart = cartService.addToCart(order);

        // Save order
        Order savedOrder = orderService.saveOrder(order);

        return ResponseEntity.ok(savedOrder);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<String> payOrder(@PathVariable String orderId) {
        Optional<Order> optionalOrder = orderService.getOrderById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus("PAID");
            order.setUpdatedAt(LocalDateTime.now());
            orderService.saveOrder(order);

            // Mark cart as paid
            cartService.markCartAsPaid(order.getUser().getId());

            return ResponseEntity.ok("Order paid successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}

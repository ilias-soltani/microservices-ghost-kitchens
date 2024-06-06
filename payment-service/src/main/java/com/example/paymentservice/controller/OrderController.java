package com.example.paymentservice.controller;

import com.example.paymentservice.authntication.AuthProxy;
import com.example.paymentservice.DTO.OrderDTO;
import com.example.paymentservice.authntication.VerifyTokenRequest;
import com.example.paymentservice.authntication.VerifyTokenResponse;
import com.example.paymentservice.entities.Order;
import com.example.paymentservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    AuthProxy authProxy;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        Order createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable String userId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}/grouped")
    public ResponseEntity<List<Order>> getGroupedOrdersByUserId(@PathVariable String userId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        List<Order> orders = orderService.getGroupedOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable String orderId, @RequestBody OrderDTO order) {
        Order updatedOrder = orderService.updateOrder(orderId, order);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/chef/{chefId}")
    public ResponseEntity<List<Order>> getOrdersByChefId(@PathVariable String chefId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));// Ajoutez cette méthode
        List<Order> orders = orderService.getOrdersByChefId(chefId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/validate")
    public ResponseEntity<Order> validateOrder(@PathVariable String orderId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        Order updatedOrder = orderService.updateOrderStatus(orderId, "valid");
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/reject")
    public ResponseEntity<Order> rejectOrder(@PathVariable String orderId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        Order updatedOrder = orderService.updateOrderStatus(orderId, "rejected");
        return ResponseEntity.ok(updatedOrder);
    }

}
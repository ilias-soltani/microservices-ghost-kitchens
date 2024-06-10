package com.example.paymentservice.controller;

import com.example.paymentservice.authntication.AuthProxy;
import com.example.paymentservice.DTO.OrderDTO;
import com.example.paymentservice.authntication.VerifyTokenRequest;
import com.example.paymentservice.authntication.VerifyTokenResponse;
import com.example.paymentservice.entities.Order;
import com.example.paymentservice.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping
    public ResponseEntity<List<Order>> getOrdersByUserId(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    //all product in one order

    @GetMapping("/user/{userId}/grouped")
    public ResponseEntity<List<Order>> getGroupedOrdersByUserId(@PathVariable String userId) {
        List<Order> orders = orderService.getGroupedOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }



    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable String orderId, @RequestBody OrderDTO order) {
        Order updatedOrder = orderService.updateOrder(orderId, order);
        return ResponseEntity.ok(updatedOrder);
    }



    @GetMapping("/chef")
    public ResponseEntity<List<Order>> getOrdersByChefId(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String chefId = responseBody.get("_id").asText();
        List<Order> orders = orderService.getOrdersByChefId(chefId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/validate")
    public ResponseEntity<Order> validateOrder(@PathVariable String orderId, @RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        Order updatedOrder = orderService.updateOrderStatus(orderId, "valid");
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/reject")
    public ResponseEntity<Order> rejectOrder(@PathVariable String orderId, @RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        Order updatedOrder = orderService.updateOrderStatus(orderId, "rejected");
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/agence")
    public ResponseEntity<Order> updateAgenceId(@PathVariable String orderId, @RequestParam String agenceId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        Order updatedOrder = orderService.updateAgenceId(orderId, agenceId);
        return ResponseEntity.ok(updatedOrder);
    }

    // New endpoint for updating statusAgence
    @PutMapping("/{orderId}/status-agence")
    public ResponseEntity<Order> updateStatusAgence(@PathVariable String orderId, @RequestParam String statusAgence,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"agency"}));
        Order updatedOrder = orderService.updateStatusAgence(orderId, statusAgence);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/user/{userId}")  // New endpoint
    public ResponseEntity<Void> deleteOrdersByUserId(@PathVariable String userId) {
        orderService.deleteOrdersByUserId(userId);
        return ResponseEntity.noContent().build();


    }    @DeleteMapping("/chef/{chefId}")  // New endpoint
    public ResponseEntity<Void> deleteOrdersByChefId(@PathVariable String chefId) {
        orderService.deleteOrdersByChefId(chefId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client","chef","agency"}));
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //getOrderByAgence

    @GetMapping("/agence")
    public ResponseEntity<List<Order>> getOrdersByAgenceId(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"agency"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String agenceId = responseBody.get("_id").asText();
        List<Order> orders = orderService.getOrdersByAgenceId(agenceId);
        return ResponseEntity.ok(orders);
    }

    // New endpoint to get orders by statusOrder for a user


    @GetMapping("/user/status/{statusOrder}")
    public ResponseEntity<List<Order>> getNonGroupedOrdersByStatusOrderForUser(@PathVariable String statusOrder,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client","chef", "agency"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        List<Order> orders = orderService.getNonGroupedOrdersByStatusOrderForUser(userId, statusOrder);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/non-grouped")  // New endpoint
    public ResponseEntity<List<Order>> getNonGroupedOrdersByUserId(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());
        String userId = responseBody.get("_id").asText();
        List<Order> orders = orderService.getNonGroupedOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }


    @PutMapping("/{orderId}/en-livraison")  // New endpoint to update statusOfOrder to "en Livraison"
    public ResponseEntity<Order> updateOrderStatusToEnLivraison(@PathVariable String orderId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        Order updatedOrder = orderService.updateOrderStatusToEnLivraison(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/delivered")
    public ResponseEntity<Order> markOrderAsDelivered(@PathVariable String orderId,@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"chef"}));
        Order updatedOrder = orderService.updateOrderStatusToDelivered(orderId);
        return ResponseEntity.ok(updatedOrder);
    }


}

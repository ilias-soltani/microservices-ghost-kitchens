package com.example.paymentservice.service;

import com.example.paymentservice.entities.Order;
import com.example.paymentservice.repository.OrderRepository;
import com.example.paymentservice.DTO.OrderDTO;
import com.example.paymentservice.DTO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setProducts(orderDTO.getProducts());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setCreatedAt(LocalDateTime.now());
        order.setGrouped(orderDTO.isGrouped()); // Indique qu'il s'agit d'une commande groupée ou individuelle
        order.setChefId(orderDTO.getChefId());
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .filter(Order::isGrouped) // Filtrez les commandes groupées
                .collect(Collectors.toList());
    }

    public List<Order> getGroupedOrdersByUserId(String userId) {
        return orderRepository.findByUserIdAndIsGroupedTrue(userId);
    }


    public Order updateOrder(String orderId, OrderDTO orderDTO) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setProducts(orderDTO.getProducts());
            order.setTotalPrice(orderDTO.getTotalPrice());
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

    public List<Order> getOrdersByChefId(String chefId) { // Ajoutez cette méthode
        return orderRepository.findByChefId(chefId);
    }


    public Order updateOrderStatus(String orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }
}

package com.example.paymentservice.service;

import com.example.paymentservice.entities.Order;
import com.example.paymentservice.repository.OrderRepository;
import com.example.paymentservice.DTO.OrderDTO;
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
        order.setGrouped(orderDTO.isGrouped());
        order.setChefId(orderDTO.getChefId());
        order.setAgenceId(orderDTO.getAgenceId());
        order.setStatusAgence(orderDTO.getStatusAgence());
        order.setAdresse(orderDTO.getAdresse());
        updateStatusOfOrder(order);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .filter(Order::isGrouped)
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
            updateStatusOfOrder(order);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

    public List<Order> getOrdersByChefId(String chefId) {
        return orderRepository.findByChefId(chefId);
    }

    // notif

    public Order updateOrderStatus(String orderId, String statusChef) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatusChef(statusChef);
            if ("rejected".equals(statusChef)) {
                order.setAgenceId(null);
                order.setStatusAgence(null);
            }
            updateStatusOfOrder(order);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order updateAgenceId(String orderId, String agenceId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setAgenceId(agenceId);
            updateStatusOfOrder(order);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

    public Order updateStatusAgence(String orderId, String statusAgence) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatusAgence(statusAgence);
            updateStatusOfOrder(order);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

    // notif

    private void updateStatusOfOrder(Order order) {
        if ("rejected".equals(order.getStatusChef())) {
            order.setStatusOfOrder("rejected");
        } else if ("valid".equals(order.getStatusChef()) && "valid".equals(order.getStatusAgence())) {
            order.setStatusOfOrder("approved");
        } else {
            order.setStatusOfOrder("invalid");
        }
    }

    public void deleteOrdersByUserId(String userId) {  // New method
        orderRepository.deleteByUserId(userId);

    }

    public void deleteOrdersByChefId(String chefId) {  // New method
        orderRepository.deleteByChefId(chefId);
    }

    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getOrdersByAgenceId(String agenceId) {
        return orderRepository.findByAgenceId(agenceId);
    }

    public List<Order> getNonGroupedOrdersByStatusOrderForUser(String userId, String statusOrder) {
        return orderRepository.findByUserIdAndStatusOfOrderAndIsGroupedFalse(userId, statusOrder);
    }

    public List<Order> getNonGroupedOrdersByUserId(String userId) { // New method
        return orderRepository.findByUserIdAndIsGroupedFalse(userId);
    }



    public Order updateOrderAdresse(String orderId, String adresse) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setAdresse(adresse);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }


    //notif

    public Order updateOrderStatusToEnLivraison(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatusOfOrder("en Livraison");
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

    public Order updateOrderStatusToDelivered(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatusOfOrder("livré");
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

}

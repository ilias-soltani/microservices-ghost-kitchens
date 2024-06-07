package com.example.paymentservice.repository;


import com.example.paymentservice.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findByChefId(String chefId);
    List<Order> findByUserIdAndIsGroupedTrue(String userId);
}


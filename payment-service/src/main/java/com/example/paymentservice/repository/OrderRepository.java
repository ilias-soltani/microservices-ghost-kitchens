package com.example.paymentservice.repository;


import com.example.paymentservice.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findByChefId(String chefId);
    List<Order> findByUserIdAndIsGroupedTrue(String userId);
    List<Order> findByAgenceId(String agenceId); // New method to find orders by agenceId
    List<Order> findByUserIdAndStatusOfOrderAndIsGroupedFalse(String userId, String statusOrder); // New method to find non-grouped orders by statusOrder for a user
    List<Order> findByUserIdAndIsGroupedFalse(String userId); // New method to find non-grouped orders by userId
    void deleteByUserId(String userId);
    void deleteByChefId(String chefId);
}


package com.esisba.orderservice.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.esisba.orderservice.entities.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
}


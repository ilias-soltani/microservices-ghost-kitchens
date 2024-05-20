package com.esisba.orderservice.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.esisba.orderservice.entities.Cart;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserIdAndPaid(String userId, boolean paid);
}


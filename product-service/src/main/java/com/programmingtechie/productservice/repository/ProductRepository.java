package com.programmingtechie.productservice.repository;

import com.programmingtechie.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByIdCategory(String idCategory);
}

package com.programmingtechie.categoryservice.repository;

import com.programmingtechie.categoryservice.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}

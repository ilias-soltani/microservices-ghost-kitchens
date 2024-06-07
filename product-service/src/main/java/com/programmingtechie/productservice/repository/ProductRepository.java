package com.programmingtechie.productservice.repository;

import com.programmingtechie.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByValidatedAndIdCategory(boolean validated, String idCategory);
    List<Product> findByValidated(boolean validated);
    List<Product> findByNameIgnoreCaseContainingAndValidated(String name, boolean validated);
    List<Product> findByWilayaAndValidated(String wilaya, boolean validated);
    List<Product> findByIdChef(String idChef);
}

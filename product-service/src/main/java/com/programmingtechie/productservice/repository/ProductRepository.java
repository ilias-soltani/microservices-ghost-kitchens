package com.programmingtechie.productservice.repository;

import com.programmingtechie.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByValidatedAndIdCategoryAndWilaya(boolean validated, String idCategory,String wilaya);
    List<Product> findByValidated(boolean validated);
    List<Product> findByNameIgnoreCaseContainingAndValidatedAndWilaya(String name, boolean validated,String wilaya);
    List<Product> findByWilayaAndValidated(String wilaya, boolean validated);
    List<Product> findByIdChef(String idChef);
    List<Product> findByIdChefAndNameIgnoreCaseContaining(String idChef,String name);

}

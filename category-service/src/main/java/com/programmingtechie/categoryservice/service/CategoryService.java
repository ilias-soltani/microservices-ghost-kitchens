package com.programmingtechie.categoryservice.service;

import com.programmingtechie.categoryservice.model.Category;
import com.programmingtechie.categoryservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
    }

    public void deleteCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
        categoryRepository.delete(category);
        log.info("Category {} is deleted", categoryId);
    }


    public Category patchCategory(String categoryId, Category category) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        // Update fields if they are not null in the request
        if (category.getName() != null) {
            existingCategory.setName(category.getName());
        }
        // Similarly update other fields as needed...

        return categoryRepository.save(existingCategory);
    }
}

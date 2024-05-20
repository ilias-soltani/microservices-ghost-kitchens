package com.programmingtechie.categoryservice.controller;

import com.programmingtechie.categoryservice.model.Category;
import com.programmingtechie.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Category getCategoryById(@PathVariable String categoryId) {
        return categoryService.getCategoryById(categoryId);
    }


    @PatchMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Category patchCategory(@PathVariable String categoryId, @RequestBody Category category) {
        return categoryService.patchCategory(categoryId, category);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable String categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }
}

package com.shopsphere.catalog.service;

import com.shopsphere.catalog.entity.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {

    Category createCategory(Category category);

    Page<Category> getCategories(String name, int page, int size, String sortBy, String direction);

    Category getCategoryById(Long id);

    void deleteCategory(Long id);

    Category updateCategory(Long id, Category category);
}
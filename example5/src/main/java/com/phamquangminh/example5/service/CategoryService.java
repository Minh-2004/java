package com.phamquangminh.example5.service;

import com.phamquangminh.example5.entity.Category;
import com.phamquangminh.example5.payloads.CategoryDTO;
import com.phamquangminh.example5.payloads.CategoryResponse;

public interface CategoryService {

    CategoryDTO getCategoryById(Long categoryId);

    CategoryDTO createCategory(Category category);

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO updateCategory(Category category, Long categoryId);

    String deleteCategory(Long categoryId);
}

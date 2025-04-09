package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.CategoryDTO;
import com.adfecomm.adfecomm.payload.CategoryResponse;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNUmber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO updateCategory(CategoryDTO category, Long categoryId);
    CategoryDTO deleteCategory(Long categoryId);
}

package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.CategoryDTO;
import com.adfecomm.adfecomm.payload.ListResponse;

public interface CategoryService {

    ListResponse getAllCategories(Integer pageNUmber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO updateCategory(CategoryDTO category, Long categoryId);
    CategoryDTO deleteCategory(Long categoryId);
}

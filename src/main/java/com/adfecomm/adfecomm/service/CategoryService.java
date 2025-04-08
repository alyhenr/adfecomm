package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.model.Category;
import com.adfecomm.adfecomm.payload.CategoryDTO;
import com.adfecomm.adfecomm.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNUmber, Integer pageSize);
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO updateCategory(CategoryDTO category, Long categoryId);
    CategoryDTO deleteCategory(Long categoryId);
}

package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Category;
import com.adfecomm.adfecomm.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        categoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException(categoryId, "category", "id", ""));

        category.setCategoryId(categoryId);
        categoryRepository.save(category);

        return category;
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository
                            .findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException(categoryId, "category", "id", ""));

        categoryRepository.delete(category);
        return "categoryId: " + categoryId + " deleted";

    }
}

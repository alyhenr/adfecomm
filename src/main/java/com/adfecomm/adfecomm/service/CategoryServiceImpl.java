package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private List<Category> categories = new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        categories.add(category);
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> categoryToUpdate = categories.stream()
            .filter(c -> c.getCategoryId().equals(categoryId))
            .findFirst();
        categoryToUpdate.ifPresentOrElse(cat -> {cat
                .setCategoryName(category.getCategoryName()); }
                , () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found."); });

        return categoryToUpdate.get();
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found."));
        categories.remove(category);
        return "categoryId: " + categoryId + " deleted";

    }
}

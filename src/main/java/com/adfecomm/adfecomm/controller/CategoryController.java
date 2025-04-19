package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.config.AppConstants;
import com.adfecomm.adfecomm.payload.CategoryDTO;
import com.adfecomm.adfecomm.payload.CategoryResponse;
import com.adfecomm.adfecomm.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_CATEGORY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category) {
        CategoryDTO newCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO categoryDTO = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryDTO);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO category
                                                ,@PathVariable Long categoryId) {
        CategoryDTO categoryDTO = categoryService.updateCategory(category, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }
}

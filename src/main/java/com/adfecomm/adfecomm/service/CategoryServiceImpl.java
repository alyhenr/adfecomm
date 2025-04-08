package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Category;
import com.adfecomm.adfecomm.payload.CategoryDTO;
import com.adfecomm.adfecomm.payload.CategoryResponse;
import com.adfecomm.adfecomm.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll().stream()
                                        .map(category -> modelMapper.map(category, CategoryDTO.class))
                                        .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categories);
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        categoryDTO.setCategoryName(categoryDTO.getCategoryName().trim());
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null) {
            throw new APIException("Category with name: '" + existingCategory.getCategoryName() + "' already exists");
        }
        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        categoryDTO.setCategoryName(categoryDTO.getCategoryName().trim());
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException(categoryId, "category", "id"));

        category.setCategoryId(categoryId);

        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository
                            .findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException(categoryId, "category", "id"));

        categoryRepository.delete(category);
        return "categoryId: " + categoryId + " deleted";

    }
}

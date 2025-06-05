package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Category;
import com.adfecomm.adfecomm.payload.CategoryDTO;
import com.adfecomm.adfecomm.payload.ListResponse;
import com.adfecomm.adfecomm.repository.CategoryRepository;
import com.adfecomm.adfecomm.util.ListResponseBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    ListResponseBuilder listResponseBuilder;

    @Override
    public ListResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable categoryPageable = ListResponseBuilder.create()
                .PageNumber(pageNumber)
                .PageSize(pageSize)
                .SortBy(sortBy)
                .SortOrder(sortOrder)
                .buildPage();
        Page<Category> categoryPage = categoryRepository.findAll(categoryPageable);
        return listResponseBuilder.createListResponse(categoryPage, CategoryDTO.class);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(null);
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
            .orElseThrow(() -> new ResourceNotFoundException(categoryId, "Category", "id"));

        category.setCategoryId(categoryId);

        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository
                            .findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException(categoryId, "Category", "id"));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);

    }
}

package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Category;
import com.adfecomm.adfecomm.payload.CategoryDTO;
import com.adfecomm.adfecomm.payload.CategoryResponse;
import com.adfecomm.adfecomm.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<CategoryDTO> categories = categoryPage.getContent().stream()
                                        .map(category -> modelMapper.map(category, CategoryDTO.class))
                                        .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categories);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
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
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository
                            .findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException(categoryId, "category", "id"));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);

    }
}

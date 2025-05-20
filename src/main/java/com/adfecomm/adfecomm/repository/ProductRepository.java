package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.Category;
import com.adfecomm.adfecomm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Product findByProductName(String productName);
    Page<Product> findByCategory(Category category, Pageable pageDetails);
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
    Page<Product> findByCategoryAndProductNameLikeIgnoreCase(Category category, String keyword, Pageable pageDetails);
}

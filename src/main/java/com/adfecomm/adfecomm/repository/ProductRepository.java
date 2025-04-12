package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.Category;
import com.adfecomm.adfecomm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductName(String productName);
    List<Product> findByCategory(Category category);
    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}

package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

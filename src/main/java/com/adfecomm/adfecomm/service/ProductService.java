package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    void createProduct(Product product);
    Product updateProduct(Product product, Long productId);
    String deleteProduct(Long productId);
}

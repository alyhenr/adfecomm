package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.model.Product;
import com.adfecomm.adfecomm.payload.ProductDTO;
import com.adfecomm.adfecomm.payload.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO createProduct(ProductDTO productDTO, Long categoryId);
    ProductDTO updateProduct(ProductDTO product, Long productId);
    ProductDTO deleteProduct(Long productId);
}

package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.model.Product;
import com.adfecomm.adfecomm.payload.ProductDTO;
import com.adfecomm.adfecomm.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO createProduct(ProductDTO productDTO, Long categoryId);

    ProductDTO updateProduct(ProductDTO product, Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image);

    ProductDTO deleteProduct(Long productId);
}

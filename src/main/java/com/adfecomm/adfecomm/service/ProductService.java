package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.ListResponse;
import com.adfecomm.adfecomm.payload.ProductDTO;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ListResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ListResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ListResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO createProduct(ProductDTO productDTO, Long categoryId);
    @Transactional
    ProductDTO updateProduct(ProductDTO product, Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image);
    @Transactional
    ProductDTO deleteProduct(Long productId);
}

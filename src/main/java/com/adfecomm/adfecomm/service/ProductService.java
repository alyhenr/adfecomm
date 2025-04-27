package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.ListResponse;
import com.adfecomm.adfecomm.payload.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ListResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ListResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ListResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO createProduct(ProductDTO productDTO, Long categoryId);

    ProductDTO updateProduct(ProductDTO product, Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image);

    ProductDTO deleteProduct(Long productId);
}

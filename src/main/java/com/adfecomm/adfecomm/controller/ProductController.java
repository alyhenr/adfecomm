package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.config.AppConstants;
import com.adfecomm.adfecomm.payload.ListResponse;
import com.adfecomm.adfecomm.payload.ProductDTO;
import com.adfecomm.adfecomm.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public/products")
    public ResponseEntity<ListResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_PRODUCT) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ListResponse> getAllProducts(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_PRODUCT) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ListResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_PRODUCT) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder));
    }


    @PostMapping("/admin/products")
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody ProductDTO productDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDTO));
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDTO, categoryId));
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @RequestBody ProductDTO product,
            @PathVariable Long productId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.updateProduct(product, productId));
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId,
            @RequestParam("image")MultipartFile image
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProductImage(productId, image));
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.deleteProduct(productId));
    }
}

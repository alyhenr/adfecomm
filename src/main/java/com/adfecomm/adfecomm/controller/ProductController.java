package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.model.Product;
import com.adfecomm.adfecomm.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @PostMapping("/public/products")
    public ResponseEntity<String> createProduct(@Valid @RequestBody Product product) {
        productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created");
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String status = productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(status);
    }

    @PutMapping("/public/products/{productId}")
    public ResponseEntity<String> updateProduct(@RequestBody Product product
                                                ,@PathVariable Long productId) {
        productService.updateProduct(product, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product with id: " + productId + " updated.");
    }
}

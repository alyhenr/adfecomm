package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Product;
import com.adfecomm.adfecomm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void createProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product, Long productId) {
        productRepository
            .findById(productId)
            .orElseThrow(() ->new ResourceNotFoundException(productId, "product", "id", ""));

        product.setProductId(productId);
        productRepository.save(product);

        return product;
    }

    @Override
    public String deleteProduct(Long productId) {
        Product product = productRepository
                            .findById(productId)
                            .orElseThrow(() ->new ResourceNotFoundException(productId, "product", "id", ""));

        productRepository.delete(product);
        return "productId: " + productId + " deleted";

    }
}

package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Cart;
import com.adfecomm.adfecomm.model.CartItem;
import com.adfecomm.adfecomm.model.Category;
import com.adfecomm.adfecomm.model.Product;
import com.adfecomm.adfecomm.payload.*;
import com.adfecomm.adfecomm.repository.CartItemRepository;
import com.adfecomm.adfecomm.repository.CartRepository;
import com.adfecomm.adfecomm.repository.CategoryRepository;
import com.adfecomm.adfecomm.repository.ProductRepository;
import com.adfecomm.adfecomm.util.ListResponseBuilder;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    private FileServiceImpl fileService;
    @Autowired
    ListResponseBuilder listResponseBuilder;
    @Autowired
    CartService cartService;

    @Override
    public ListResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable productsPageable = ListResponseBuilder.create()
                .PageNumber(pageNumber)
                .PageSize(pageSize)
                .SortBy(sortBy)
                .SortOrder(sortOrder)
                .buildPage();

        Page<Product> productPage = productRepository.findAll(productsPageable);
        return listResponseBuilder.createListResponse(productPage, ProductDTO.class);
    }

    public ListResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable productsPageable = ListResponseBuilder.create()
                .PageNumber(pageNumber)
                .PageSize(pageSize)
                .SortBy(sortBy)
                .SortOrder(sortOrder)
                .buildPage();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(categoryId, "Category", "id"));
        Page<Product> productPage = productRepository.findByCategory(category, productsPageable);
        return  listResponseBuilder.createListResponse(productPage, ProductDTO.class);
    }

    @Override
    public ListResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable productsPageable = ListResponseBuilder.create()
                .PageNumber(pageNumber)
                .PageSize(pageSize)
                .SortBy(sortBy)
                .SortOrder(sortOrder)
                .buildPage();
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%", productsPageable);
        return listResponseBuilder.createListResponse(productPage, ProductDTO.class);
    }

    private Product saveProduct(Product product) {
        Product existingProduct = productRepository.findByProductName(product.getProductName());

        if (existingProduct != null && existingProduct.getCategory().equals(product.getCategory())) {
            throw new APIException("Product with name: '" + existingProduct.getProductName() + "' already exists in this category: " + product.getCategory().getCategoryName());
        }

        return productRepository.save(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        productDTO.setProductName(productDTO.getProductName().trim());
        Long categoryId = productDTO.getCategory().getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(categoryId, "Category", "id"));
        productDTO.setCategory(category);
        Product product = saveProduct(modelMapper.map(productDTO, Product.class));
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(categoryId, "Category", "id"));
        productDTO.setProductName(productDTO.getProductName().trim());
        productDTO.setCategory(category);

        Product product = saveProduct(modelMapper.map(productDTO, Product.class));
        productDTO.setProductId(product.getProductId());

        return productDTO;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        productDTO.setProductName(productDTO.getProductName().trim());
        Category category = productDTO.getCategory();
        if (category != null) {
            Long categoryId = category.getCategoryId();
            Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(categoryId, "Category", "id"));
            productDTO.setCategory(foundCategory);
        }

        Product product = modelMapper.map(productDTO, Product.class);
        productRepository
            .findById(productId)
            .orElseThrow(() ->new ResourceNotFoundException(productId, "Product", "id"));

        product.setProductId(productId);
        saveProduct(product);
        productDTO.setProductId(productId);

        //TODO
        /*
        Update carts which have this product
         */
        updateCartsWithProduct(productId, product);
        return productDTO;
    }

    private void updateCartsWithProduct(Long productId, Product product) {
        List<Cart>  carts = cartRepository.findCartsByProduct(productId);
        for (Cart c: carts) {
            CartItem cartItem = cartItemRepository.findByCartIdAndProductId(c.getCartId(), productId);
            cartItem.setProduct(product);
            cartItem.setPrice(product.getPrice());
            cartItem.setDiscount(product.getDiscount());
            cartItemRepository.save(cartItem);

            c.setTotalPrice(cartService.calcCartTotalPrice(c));
            cartRepository.save(c);
        }
    }

    @Override
    @Transactional
    public ProductDTO updateProductImage(Long productId, MultipartFile image) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->new ResourceNotFoundException(productId, "product", "id"));

        try {
            String url = fileService.uploadImage("", image);
            product.setImageUrl(url);
            return modelMapper.map(saveProduct(product), ProductDTO.class);
        } catch (Exception e) {
            throw new APIException("Fail to update image of product with id: " + productId + " - Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository
                            .findById(productId)
                            .orElseThrow(() ->new ResourceNotFoundException(productId, "product", "id"));
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        List<Cart> carts = cartRepository.findCartsByProduct(productId);
        for (Cart c: carts) {
            cartService.deleteProdFromCart(productId, c.getCartId());
        }

        productRepository.delete(product);
        return productDTO;

    }
}

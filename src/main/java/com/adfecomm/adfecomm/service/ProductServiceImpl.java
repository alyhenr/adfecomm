package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Category;
import com.adfecomm.adfecomm.model.Product;
import com.adfecomm.adfecomm.payload.CategoryDTO;
import com.adfecomm.adfecomm.payload.CategoryResponse;
import com.adfecomm.adfecomm.payload.ProductDTO;
import com.adfecomm.adfecomm.payload.ProductResponse;
import com.adfecomm.adfecomm.repository.CategoryRepository;
import com.adfecomm.adfecomm.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileServiceImpl fileService;

    private Pageable createPage(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByOrderBy = sortOrder.equalsIgnoreCase("asc")
                ?  Sort.by(sortBy).ascending()
                :  Sort.by(sortBy).descending();

        return PageRequest.of(pageNumber, pageSize, sortByOrderBy);
    }

    private ProductResponse createProductResponse(Page<Product> productPage) {
        List<ProductDTO> products = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(products);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = createPage(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        return  createProductResponse(productPage);
    }

    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = createPage(pageNumber, pageSize, sortBy, sortOrder);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(categoryId, "Category", "id"));
        Page<Product> productPage = productRepository.findByCategory(category, pageDetails);
        return  createProductResponse(productPage);
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = createPage(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%", pageDetails);
        return createProductResponse(productPage);
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
        return productDTO;
    }

    @Override
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
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository
                            .findById(productId)
                            .orElseThrow(() ->new ResourceNotFoundException(productId, "product", "id"));
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        productRepository.delete(product);
        return productDTO;

    }
}

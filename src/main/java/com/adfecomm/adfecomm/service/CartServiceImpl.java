package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.payload.CartDTO;
import com.adfecomm.adfecomm.payload.ProductDTO;
import com.adfecomm.adfecomm.repository.ProductRepository;
import com.adfecomm.adfecomm.security.service.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductRepository productRepository;


    /*
        1. Find product, if not exists throws APIException
        2. Check if asked quantity is valid
        3. Check if user already have a cart, if not creates it
        4. Add product to cart if not already, otherwise updates quantity, price and discount
     */
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        ProductDTO productDTO = modelMapper.map(
                productRepository.findById(productId)
                        .orElseThrow(() -> new APIException("Product not found with id: " + productId))
                , ProductDTO.class);

        if (productDTO.getQuantity() < quantity)
            throw new APIException("Asked quantity exceeds current product quantity available. Asked: " + quantity + ", Available: " + productDTO.getQuantity());


//        UserDetailsImpl userDetails =
        return null;
    }
}

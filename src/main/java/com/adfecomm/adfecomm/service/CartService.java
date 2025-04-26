package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.CartDTO;

import java.util.List;

public interface CartService {
    List<CartDTO> getAllCarts();
    CartDTO addProductToCart(Long productId, Integer quantity);
}

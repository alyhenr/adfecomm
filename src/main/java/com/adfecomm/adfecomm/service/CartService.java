package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
}

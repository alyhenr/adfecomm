package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.CartDTO;
import com.adfecomm.adfecomm.payload.ListResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface CartService {
    ListResponse getAllCarts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CartDTO addProductToCart(Long productId, Integer quantity);
    CartDTO getCartByUser();
}

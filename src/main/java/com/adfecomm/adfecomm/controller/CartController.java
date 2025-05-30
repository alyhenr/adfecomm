package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.config.AppConstants;
import com.adfecomm.adfecomm.payload.APIResponse;
import com.adfecomm.adfecomm.payload.CartDTO;
import com.adfecomm.adfecomm.payload.ListResponse;
import com.adfecomm.adfecomm.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/admin/carts")
    public ResponseEntity<ListResponse> getAllCarts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_CART) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getAllCarts(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/users/carts/user")
    public ResponseEntity<CartDTO> getCartById() {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartByUser());
    }

    @PostMapping("/users/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<?> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity
    ) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(Objects.isNull(cartDTO)
                ? new APIResponse("Cart is empty", true)
                : cartDTO);
    }

    @PostMapping("/users/carts")
    public ResponseEntity<?> createCart(
            @RequestBody CartDTO cartDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(cartDTO));
    }

    @DeleteMapping("/users/carts/{cartId}/product/{productId}")
    public ResponseEntity<CartDTO> deleteProdFromCart(
            @PathVariable Long productId,
            @PathVariable Long cartId
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(cartService.deleteProdFromCart(productId, cartId));
    }
}

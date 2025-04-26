package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.payload.CartDTO;
import com.adfecomm.adfecomm.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/admin/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @PostMapping("/users/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addProductToCart(productId, quantity));
    }
}

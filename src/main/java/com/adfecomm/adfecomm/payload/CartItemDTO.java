package com.adfecomm.adfecomm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private Double productPrice;
    private Double discount;

    public CartItemDTO(Long cartItemId, Long productId, Integer quantity, Double productPrice, Double discount) {
        this.productPrice = productPrice;
        this.discount = discount;
        this.quantity = quantity;
        this.productId = productId;
        this.cartItemId = cartItemId;
    }
}

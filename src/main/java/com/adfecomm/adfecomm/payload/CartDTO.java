package com.adfecomm.adfecomm.payload;

import com.adfecomm.adfecomm.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long cartId;
    private Double totalPrice = 0.0;
    private List<CartItemDTO> cartItems = new ArrayList<>();
}

package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.model.Cart;
import com.adfecomm.adfecomm.model.CartItem;
import com.adfecomm.adfecomm.model.Product;
import com.adfecomm.adfecomm.payload.CartDTO;
import com.adfecomm.adfecomm.payload.CartItemDTO;
import com.adfecomm.adfecomm.payload.ProductDTO;
import com.adfecomm.adfecomm.repository.CartItemRepository;
import com.adfecomm.adfecomm.repository.CartRepository;
import com.adfecomm.adfecomm.repository.ProductRepository;
import com.adfecomm.adfecomm.security.service.UserDetailsImpl;
import com.adfecomm.adfecomm.util.AuthUtil;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    AuthUtil authUtil;

    /*
        1. Find product, if not exists throws APIException
        2. Check if asked quantity is valid
        3. Check if user already have a cart, if not creates it
        4. Add product to cart if not already, otherwise updates quantity, price and discount
     */
    @Override
    public CartDTO addProductToCart(Long productId, @NotNull  Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new APIException("Product not found with id: " + productId));

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        if (productDTO.getQuantity() < quantity)
            throw new APIException("Asked quantity exceeds current product quantity available. Asked: " + quantity + ", Available: " + productDTO.getQuantity());

        Cart cart = getUserCart();
        CartItem cartItem = cartItemRepository.findByCartItemIdAndProductId(cart.getCartId(), productId);
        if (Objects.isNull(cartItem)) {
            cartItem = new CartItem(cart, product, quantity, product.getDiscount(), product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(productDTO.getDiscount());
            cartItem.setPrice(productDTO.getPrice());
        }
        cartItemRepository.save(cartItem);
        List<CartItem> cartItemList = cart.getCartItems();
        cartItemList.add(cartItem);
        cart.setCartItems(cartItemList);
        cart.setTotalPrice(cart.getTotalPrice() + (productDTO.getPrice() * (1 - productDTO.getDiscount()/100)) * quantity);

        CartDTO cartDTO = modelMapper.map(cartRepository.save(cart), CartDTO.class);
        cartDTO.setCartItemsDTO(cart.getCartItems()
                .stream()
                .map(c -> new CartItemDTO(c.getCartItemId()
                        , c.getCart().getCartId()
                        , c.getProduct().getProductId()
                        , c.getQuantity()
                        , c.getDiscount()
                        , c.getPrice()))
                .toList());
        return cartDTO;

    }

    private Cart getUserCart() {
        Cart cart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (cart != null) {
            return cart;
        }
        return cartRepository.save(new Cart(authUtil.loggedInUser(), 0.00));
    }
}

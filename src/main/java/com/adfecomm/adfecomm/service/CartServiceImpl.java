package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Cart;
import com.adfecomm.adfecomm.model.CartItem;
import com.adfecomm.adfecomm.model.Product;
import com.adfecomm.adfecomm.model.User;
import com.adfecomm.adfecomm.payload.CartDTO;
import com.adfecomm.adfecomm.payload.ListResponse;
import com.adfecomm.adfecomm.payload.ProductDTO;
import com.adfecomm.adfecomm.repository.CartItemRepository;
import com.adfecomm.adfecomm.repository.CartRepository;
import com.adfecomm.adfecomm.repository.ProductRepository;
import com.adfecomm.adfecomm.util.AuthUtil;
import com.adfecomm.adfecomm.util.ListResponseBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    ListResponseBuilder listResponseBuilder;
    @Override
    public ListResponse getAllCarts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable cartPageable = ListResponseBuilder.create()
                .PageNumber(pageNumber)
                .PageSize(pageSize)
                .SortBy(sortBy)
                .SortOrder(sortOrder)
                .buildPage();
        Page<Cart> cartPage = cartRepository.findAll(cartPageable);
        return listResponseBuilder.createListResponse(cartPage, CartDTO.class);
    }

    /*
        1. Find product, if not exists throws APIException
        2. Check if asked quantity is valid
        3. Check if user already have a cart, if not creates it
        4. Add product to cart if not already, otherwise updates quantity, price and discount
     */
    @Transactional
    @Override
    public CartDTO addProductToCart(Long productId, @NotNull @PositiveOrZero  Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new APIException("Product not found with id: " + productId));

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        Cart cart = getUserCart();
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), productId);

        Double cartTotalPrice;
        if (Objects.isNull(cartItem)) {
            cartTotalPrice = cart.getTotalPrice();
            if (quantity > 0) {
                cartItem = new CartItem(cart, product, quantity, product.getDiscount(), product.getPrice());
                List<CartItem> cartItemList = cart.getCartItems();
                cartItemList.add(cartItem);
                cart.setCartItems(cartItemList);
            } else return null;
        } else {
            cartTotalPrice = cart.getTotalPrice()
                    - cartItem.getPrice() * (1 - cartItem.getDiscount()/100) * Math.max(cartItem.getQuantity(), 0);
            if (quantity <= 0) {
                cartItemRepository.delete(cartItem);
                //Check if cart is empty, if so, delete it
                if (isCartEmpty()) {
                    cartRepository.delete(cart);
                    return null;
                }
                return modelMapper.map(cartRepository.save(cart), CartDTO.class);
            }
            cartItem.setQuantity(quantity);
            cartItem.setDiscount(productDTO.getDiscount());
            cartItem.setPrice(productDTO.getPrice());
        }

        if (productDTO.getQuantity() < quantity)
            throw new APIException("Asked quantity exceeds current product quantity available. Asked: " + cartItem.getQuantity() + ", Available: " + productDTO.getQuantity());

        cartItemRepository.save(cartItem);
//        List<CartItem> cartItemList = cart.getCartItems();
//        cartItemList.add(cartItem);
//        cart.setCartItems(cartItemList);
        cart.setTotalPrice(cartTotalPrice + (productDTO.getPrice() * (1 - productDTO.getDiscount()/100)) * quantity);

        return modelMapper.map(cartRepository.save(cart), CartDTO.class);
    }

    @Transactional
    @Override
    public CartDTO createCart(CartDTO cartDTO) {
        cleanCart();
        CartDTO createdCartDTO = new CartDTO();
        for (CartItem cartItem: cartDTO.getCartItems()) {
            Long productId = cartItem.getProduct().getProductId();
            int quantity = cartItem.getQuantity();

            createdCartDTO = addProductToCart(productId, quantity);
        }
        return createdCartDTO;
    }

    private void cleanCart() {
        Cart cart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (cart != null) {
            cartItemRepository.deleteByCartId(cart.getCartId());
        }
    }

    private Cart getUserCart() {
        Cart cart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (cart != null) {
            return cart;
        }
        return cartRepository.save(new Cart(authUtil.loggedInUser(), 0.00));
    }

    private boolean isCartEmpty() {
        Cart cart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (cart != null) {
            for (CartItem ci: cart.getCartItems()) {
                if (ci.getQuantity() > 0) return false;
            }
        }
        return true;
    }

    @Override
    public CartDTO getCartByUser() {
        Cart cart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (Objects.isNull(cart)) throw new ResourceNotFoundException("Cart");
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    @Transactional
    public CartDTO deleteProdFromCart(Long productId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(cartId, "Cart", "cartId"));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException(productId, "Product", "productId");
        }

        cart.getCartItems().removeIf(ci -> ci.getProduct().getProductId().equals(productId));
        cart.setTotalPrice(calcCartTotalPrice(cart));
        cartItemRepository.delete(cartItem); // Optional if orphanRemoval=true

        if (isCartEmpty()) {
            cartRepository.delete(cart);
        }

        return modelMapper.map(cart, CartDTO.class);
    }

    public double calcCartTotalPrice(Cart cart) {
        double cartTotalPrice = 0.0;
        for (CartItem ci: cart.getCartItems()) {
            cartTotalPrice += ci.getPrice() * (1-ci.getDiscount()) * ci.getQuantity();
        }
        return cartTotalPrice;
    }

    @Override
    public void clearUserCart() {
        User user = authUtil.loggedInUser();
        Cart cart = cartRepository.findCartByEmail(user.getEmail());
        for (CartItem cartItem: cart.getCartItems()) {
            cartItemRepository.delete(cartItem);
        }
        cartRepository.delete(cart);
    }
}

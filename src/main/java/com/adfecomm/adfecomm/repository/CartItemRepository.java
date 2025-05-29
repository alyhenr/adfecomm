package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId = ?1 and ci.product.productId = ?2")
    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    @Modifying
    @Query("DELETE from CartItem ci WHERE ci.cart.cartId = ?1")
    void deleteByCartId(Long cartId);
}

package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);
    @Query("SELECT DISTINCT c FROM Cart c JOIN c.cartItems ci WHERE ci.product.productId = ?1")
    List<Cart> findCartsByProduct(Long productId);

}

package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.payment.pgPaymentId = ?1")
    Optional<Order> findByPaymentPGPaymentId(String pgPaymentId);
}

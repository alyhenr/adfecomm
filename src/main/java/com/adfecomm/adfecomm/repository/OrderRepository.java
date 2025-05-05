package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

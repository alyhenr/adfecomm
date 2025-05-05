package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.OrderDTO;
import jakarta.transaction.Transactional;

public interface OrderService {
    @Transactional
    OrderDTO placeOrder(OrderDTO orderDTO, String paymentMethod);
}

package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.OrderDTO;
import com.adfecomm.adfecomm.payload.OrderRequest;
import jakarta.transaction.Transactional;

public interface OrderService {
    @Transactional
    OrderDTO placeOrder(String paymentMethod, OrderRequest orderRequest);
    OrderDTO confirmOrderPayment(String pgPaymentId, String pgResponseMessage);
}

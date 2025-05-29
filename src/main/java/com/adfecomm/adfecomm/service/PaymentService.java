package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.model.Order;
import com.adfecomm.adfecomm.model.Payment;
import com.adfecomm.adfecomm.payload.PaymentDTO;

public interface PaymentService {
    Payment createPayment(String paymentMethod, Order order, PaymentDTO paymentDTO);
}

package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.PaymentDTO;

public interface PaymentService {
    PaymentDTO managePayment(String paymentMethod, Long orderId);
}

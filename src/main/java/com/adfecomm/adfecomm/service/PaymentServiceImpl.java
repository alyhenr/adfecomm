package com.adfecomm.adfecomm.service;


import com.adfecomm.adfecomm.payload.PaymentDTO;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentDTO managePayment(String paymentMethod, Long orderId) {
        return null;
    }
}

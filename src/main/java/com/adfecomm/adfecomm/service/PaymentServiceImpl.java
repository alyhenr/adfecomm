package com.adfecomm.adfecomm.service;


import com.adfecomm.adfecomm.model.Order;
import com.adfecomm.adfecomm.model.Payment;
import com.adfecomm.adfecomm.payload.PaymentDTO;
import com.adfecomm.adfecomm.repository.OrderRepository;
import com.adfecomm.adfecomm.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Payment createPayment(String paymentMethod, Order order, PaymentDTO paymentDTO) {
        return paymentRepository.save(new Payment(
                order,
                paymentMethod,
                paymentDTO.getPgPaymentId(),
                paymentDTO.getPgStatus(),
                paymentDTO.getPgResponseMessage()));
    }
}

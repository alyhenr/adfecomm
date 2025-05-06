package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.payload.OrderDTO;
import com.adfecomm.adfecomm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    OrderService orderService;
    @PostMapping("/users/orders/payments/{paymentMethod}")
    public ResponseEntity<?> createOrder(@PathVariable String paymentMethod, @RequestBody OrderDTO orderDTO) {
        Long addressId = orderDTO.getAddressId();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(paymentMethod, addressId));
    }
}

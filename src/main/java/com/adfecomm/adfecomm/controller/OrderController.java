package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.payload.OrderDTO;
import com.adfecomm.adfecomm.service.OrderService;
import com.adfecomm.adfecomm.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    AuthUtil authUtil;

    @Autowired
    OrderService orderService;
    @PostMapping("/users/orders/payments/{paymentMethod}")
    public ResponseEntity<?> createOrder(@PathVariable String paymentMethod, @Valid @RequestBody OrderDTO orderDTO) {
        String userEmailId = authUtil.loggedInEmail();
        orderDTO.setEmail(userEmailId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(orderDTO, paymentMethod));
    }
}

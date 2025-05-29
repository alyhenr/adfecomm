package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.payload.APIResponse;
import com.adfecomm.adfecomm.payload.OrderDTO;
import com.adfecomm.adfecomm.payload.StripePaymentDTO;
import com.adfecomm.adfecomm.service.OrderService;
import com.adfecomm.adfecomm.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    StripeService stripeService;

    @PostMapping("/users/orders/payments/{paymentMethod}")
    public ResponseEntity<?> createOrder(@PathVariable String paymentMethod, @RequestBody OrderDTO orderDTO) {
        Long addressId = orderDTO.getAddressId();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(paymentMethod, addressId));
    }

    @PostMapping("/users/order/payments/stripe")
    public ResponseEntity<?> createStripeIntent(@RequestBody StripePaymentDTO stripePaymentDTO) {
        try {
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(stripePaymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse(paymentIntent.getClientSecret(), true));
        } catch (StripeException ex) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse("failed to create payment intention: " + ex.getMessage(), false));
        }
    }

}

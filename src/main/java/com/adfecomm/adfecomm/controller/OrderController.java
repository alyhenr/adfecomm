package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.payload.*;
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

    @PostMapping("/users/orders/{paymentMethod}")
    public ResponseEntity<?> createOrder(@PathVariable String paymentMethod, @RequestBody OrderRequest orderRequest) {
        Long addressId = orderRequest.getAddressId();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(paymentMethod, orderRequest));
    }

    @PostMapping("/users/order/payments/stripe")
    public ResponseEntity<?> createStripeIntent(@RequestBody StripePaymentDTO stripePaymentDTO) {
        try {
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(stripePaymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new StripeIntentResponse(paymentIntent.getClientSecret(), paymentIntent.getId(), true));
        } catch (StripeException ex) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse("failed to create payment intention: " + ex.getMessage(), false));
        }
    }

    @PostMapping("/users/order/payments/confirm/{pgPaymentId}/{pgResponseMessage}")
    public ResponseEntity<?> confirmOrderPayment(@PathVariable String pgPaymentId,@PathVariable String pgResponseMessage) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(orderService.confirmOrderPayment(pgPaymentId, pgResponseMessage));
    }

}

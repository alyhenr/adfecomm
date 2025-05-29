package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.payload.StripePaymentDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeService {
    PaymentIntent createPaymentIntent(StripePaymentDTO stripePaymentDTO) throws StripeException;
}

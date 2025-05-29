package com.adfecomm.adfecomm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StripeIntentResponse {
    private String clientSecret;
    private String paymentId;
    private boolean success;
}

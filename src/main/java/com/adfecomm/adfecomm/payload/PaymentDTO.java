package com.adfecomm.adfecomm.payload;

import com.adfecomm.adfecomm.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private String paymentMethod;
    //Payment gateway information
    private String pgPaymentId;
    private OrderStatus pgStatus;
    private String pgResponseMessage;

    public PaymentDTO(String paymentMethod, String pgPaymentId, OrderStatus pgStatus, String pgResponseMessage) {
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
    }
}

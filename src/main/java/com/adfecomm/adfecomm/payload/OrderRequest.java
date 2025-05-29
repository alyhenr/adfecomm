package com.adfecomm.adfecomm.payload;

import com.adfecomm.adfecomm.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long addressId;
    private String pgPaymentId;
    private OrderStatus pgStatus;
    private String pgResponseMessage;
}

package com.adfecomm.adfecomm.payload;

import com.adfecomm.adfecomm.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private LocalDate orderDate;
    private List<OrderItemDTO> orderItems;
    private Long addressId;
    private PaymentDTO payment;
}

package com.adfecomm.adfecomm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Join;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
    })
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotBlank
    private String paymentMethod;

    //Payment gateway information
    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName; //External service name (Stripe for example)

    public Payment(Long paymentId, Order order, String paymentMethod) {
        this.paymentId = paymentId;
        this.order = order;
        this.paymentMethod = paymentMethod;
    }
}

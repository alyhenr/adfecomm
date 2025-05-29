package com.adfecomm.adfecomm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Email
    @Column
    private String email;

    private Double totalPrice;
    private LocalDate orderDate;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @OneToMany(cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
    }, mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = true)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
}

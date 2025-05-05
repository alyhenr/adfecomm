package com.adfecomm.adfecomm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Email
    @Column
    private String email;

    private Double totalAmount;
    private OrderStatus orderStatus;
    private LocalDate orderDate;

    @OneToMany(cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
    }, mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();
    @OneToOne
    private Payment payment;
    @ManyToOne
    private Address address;
}

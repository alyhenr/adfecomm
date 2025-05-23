package com.adfecomm.adfecomm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(min = 5, message = "Product name must have at least 5 characters")
    private String productName;

    @Size(min = 10, message = "Product name must have at least 10 characters")
    @Column(length = 1000)
    private String description;

    @NotNull
    @Positive
    private double price;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    private String imageUrl;

    @PositiveOrZero
    private Double discount;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;
}
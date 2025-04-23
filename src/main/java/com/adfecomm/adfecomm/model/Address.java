package com.adfecomm.adfecomm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    private String city;

    @NotBlank
    private String state; //abbreviation

    @NotBlank
    private String zipCode;

    @NotBlank
    private Integer number;

    @Size(min = 10, message = "Minimo de 10 caracteres")
    private String reference;

    @Size(min = 3, message = "Minimo de 3 caracteres")
    private String buildingName;

    @NotBlank
    private String streetName;

    @NotBlank
    @Setter(AccessLevel.PRIVATE)
    private String country = "Brasil"; //Default, only country accepted for now

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Override
    public String toString() {
        return streetName + ", " + number + ", " + reference + ", " + city + ", " + state + ", " + country;
    }
}

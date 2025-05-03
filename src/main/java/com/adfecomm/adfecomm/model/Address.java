package com.adfecomm.adfecomm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Integer number;

    private String reference;

    private String buildingName;

    @NotBlank
    private String streetName;

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

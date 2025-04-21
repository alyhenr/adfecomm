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

    private String reference;

    @NotBlank
    private String streetName;

    @NotBlank
    @Setter(AccessLevel.PRIVATE)
    private String county = "Brasil"; //Default, only country accepted for now

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private AppUser appUser;

    @Override
    public String toString() {
        return streetName + ", " + number + ", " + reference + ", " + city + ", " + state + ", " + county;
    }
}

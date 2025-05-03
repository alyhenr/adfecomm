package com.adfecomm.adfecomm.payload;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long addressId;
    private String city;
    private String state; //abbreviation
    private String zipCode;
    private Integer number;
    private String reference;
    private String buildingName;
    private String streetName;
    private String country = "Brasil";
}

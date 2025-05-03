package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.model.Address;
import com.adfecomm.adfecomm.model.User;
import com.adfecomm.adfecomm.payload.AddressDTO;
import com.adfecomm.adfecomm.payload.ListResponse;

import java.util.List;

public interface AddressService {
    public AddressDTO addAddressToUser(User user, AddressDTO addressDTO);
    ListResponse getAllAddress(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    List<Address> getUserAddresses();
}

package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.model.Address;
import com.adfecomm.adfecomm.model.User;
import com.adfecomm.adfecomm.payload.AddressDTO;
import com.adfecomm.adfecomm.payload.ListResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    public AddressDTO addAddressToUser(User user, AddressDTO addressDTO);
    ListResponse getAllAddress(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    List<Address> getUserAddresses();
    AddressDTO getAddressById(Long addressId);
    AddressDTO getUserAddressById(Long addressId);
    AddressDTO updateUserAddress(Long addressId, AddressDTO addressDTO);
    AddressDTO deleteUserAddress(Long addressId);
}

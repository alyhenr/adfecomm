package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Address;
import com.adfecomm.adfecomm.model.User;
import com.adfecomm.adfecomm.payload.APIResponse;
import com.adfecomm.adfecomm.payload.AddressDTO;
import com.adfecomm.adfecomm.payload.ListResponse;
import com.adfecomm.adfecomm.repository.AddressRepository;
import com.adfecomm.adfecomm.repository.UserRepository;
import com.adfecomm.adfecomm.util.AuthUtil;
import com.adfecomm.adfecomm.util.ListResponseBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ListResponseBuilder listResponseBuilder;

    @Override
    public ListResponse getAllAddress(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable addressPageable = ListResponseBuilder.create()
                .PageNumber(pageNumber)
                .PageSize(pageSize)
                .SortBy(sortBy)
                .SortOrder(sortOrder)
                .buildPage();

        Page<Address> addressPage = addressRepository.findAll(addressPageable);
        return listResponseBuilder.createListResponse(addressPage, AddressDTO.class);
    }

    @Override
    public List<Address> getUserAddresses() {
        User user = authUtil.loggedInUser();
        return addressRepository.findAddressByUserId(user.getUserId());
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        System.out.println("AddressId received: " + addressId);
        return modelMapper.map(
                addressRepository.findById(addressId)
                    .orElseThrow(() -> new ResourceNotFoundException(addressId, "Address", "addressId"))
                , AddressDTO.class);
    }

    @Override
    public AddressDTO getUserAddressById(Long addressId) {
        User user = authUtil.loggedInUser();
        Address address = addressRepository.findAddressByUserIdAndAddressId(user.getUserId(), addressId);
        if (Objects.isNull(address)) throw new ResourceNotFoundException(addressId, "Address for user", "addressId");
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO addAddressToUser(User user, AddressDTO addressDTO) {
        List<Address> addressList = user.getAddresses();
        Address address = modelMapper.map(addressDTO, Address.class);

        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);

        addressRepository.save(address);
        userRepository.save(user);

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO updateUserAddress(Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(addressId, "Address", "addressId"));

        User user = authUtil.loggedInUser();
        if (!Objects.equals(user, address.getUser())) throw new APIException("Address does not belongs to logged user");

        addressDTO.setAddressId(addressId);
        Address updatedAddress = modelMapper.map(addressDTO, Address.class);
        updatedAddress.setUser(address.getUser());
        return modelMapper.map(addressRepository.save(updatedAddress), AddressDTO.class);
    }

    @Override
    public AddressDTO deleteUserAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(addressId, "Address", "addressId"));

        User user = authUtil.loggedInUser();
        if (!Objects.equals(user, address.getUser())) throw new APIException("Address does not belongs to logged user");

        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
        addressRepository.delete(address);
        return addressDTO;
    }
}

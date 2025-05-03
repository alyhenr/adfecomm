package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.model.Address;
import com.adfecomm.adfecomm.model.User;
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
    public AddressDTO addAddressToUser(User user, AddressDTO addressDTO) {
        List<Address> addressList = user.getAddresses();
        Address address = modelMapper.map(addressDTO, Address.class);

        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);

        System.out.println(address.toString());

        addressRepository.save(address);
        userRepository.save(user);

        return modelMapper.map(address, AddressDTO.class);
    }
}

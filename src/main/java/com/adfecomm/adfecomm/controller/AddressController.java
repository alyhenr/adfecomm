package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.config.AppConstants;
import com.adfecomm.adfecomm.model.User;
import com.adfecomm.adfecomm.payload.AddressDTO;
import com.adfecomm.adfecomm.service.AddressService;
import com.adfecomm.adfecomm.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    AuthUtil authUtil;

    @Autowired
    AddressService addressService;

    @GetMapping("/admin/addresses")
    public ResponseEntity<?> getAllAddress(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_ADDRESS) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ) {
        return ResponseEntity.ok().body(
                addressService.getAllAddress(pageNumber, pageSize, sortBy, sortOrder)
        );
    }

    @GetMapping("/users/addresses/user")
    public ResponseEntity<?> getUserAddresses() {
        return ResponseEntity.ok().body(addressService.getUserAddresses());
    }

    @PostMapping("/users/addresses/user/address")
    public ResponseEntity<?> addAddressToUser(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                addressService.addAddressToUser(user, addressDTO)
        );
    }
}

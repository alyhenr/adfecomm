package com.adfecomm.adfecomm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adfecomm.adfecomm.payload.APIResponse;
import com.adfecomm.adfecomm.payload.AddressDTO;
import com.adfecomm.adfecomm.payload.UserDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/user/profile")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(new APIResponse("User fetched successfully!", true));
    }

    @PutMapping("/user/profile")
    public ResponseEntity<?> updateCurrentUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(new APIResponse("User updated successfully!", true));
    }

    @PutMapping("/user/address")
    public ResponseEntity<?> updateCurrentUserAddress(@RequestBody AddressDTO address) {
        return ResponseEntity.ok(new APIResponse("Address updated successfully!", true));
    }

    @GetMapping("/user/purchases")
    public ResponseEntity<?> getCurrentUserPurchases() {
        return ResponseEntity.ok(new APIResponse("Purchases fetched successfully!", true));
    }

    @GetMapping("/user/orders")
    public ResponseEntity<?> getCurrentUserOrders() {
        return ResponseEntity.ok(new APIResponse("Orders fetched successfully!", true));
    }
}

package com.adfecomm.adfecomm.security.dto;

import com.adfecomm.adfecomm.payload.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private UserDTO user;
    private int expiresIn;
}

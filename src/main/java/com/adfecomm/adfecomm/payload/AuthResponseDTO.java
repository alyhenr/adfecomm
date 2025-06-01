package com.adfecomm.adfecomm.payload;
import com.adfecomm.adfecomm.security.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private int expiresIn;
    private UserDTO user;
}
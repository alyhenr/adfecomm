package com.adfecomm.adfecomm.security.auth;

import com.adfecomm.adfecomm.security.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthenticationStrategy {
    ResponseEntity<?> authenticate(LoginRequest loginRequest, Authentication authentication);
    String extractToken(jakarta.servlet.http.HttpServletRequest request);
    boolean supports(String protocol);
} 
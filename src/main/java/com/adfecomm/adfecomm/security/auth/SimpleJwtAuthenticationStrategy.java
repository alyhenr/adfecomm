package com.adfecomm.adfecomm.security.auth;

import com.adfecomm.adfecomm.security.dto.LoginRequest;
import com.adfecomm.adfecomm.security.dto.LoginResponse;
import com.adfecomm.adfecomm.security.dto.UserDTO;
import com.adfecomm.adfecomm.security.jwt.JwtUtils;
import com.adfecomm.adfecomm.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SimpleJwtAuthenticationStrategy implements AuthenticationStrategy {

    private final JwtUtils jwtUtils;
    private final String tokenTransport;

    public SimpleJwtAuthenticationStrategy(
            JwtUtils jwtUtils,
            @Value("${spring.security.token.transport}") String tokenTransport) {
        this.jwtUtils = jwtUtils;
        this.tokenTransport = tokenTransport;
    }

    @Override
    public ResponseEntity<?> authenticate(LoginRequest loginRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateTokenFromEmail(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserDTO userDTO = new UserDTO(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );

        if ("COOKIES".equalsIgnoreCase(tokenTransport)) {
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new LoginResponse(userDTO, jwtUtils.getJwtExpirationMs()));
        } else {
            // Default to header
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(new LoginResponse(userDTO, jwtUtils.getJwtExpirationMs()));
        }
    }

    @Override
    public String extractToken(jakarta.servlet.http.HttpServletRequest request) {
        if ("COOKIES".equalsIgnoreCase(tokenTransport)) {
            return jwtUtils.getJwtFromCookies(request);
        } else {
            return jwtUtils.getJwtFromHeader(request);
        }
    }

    @Override
    public boolean supports(String protocol) {
        return "SIMPLE_AUTH".equalsIgnoreCase(protocol);
    }
} 
package com.adfecomm.adfecomm.security.auth;

import com.adfecomm.adfecomm.security.dto.LoginRequest;
import com.adfecomm.adfecomm.security.dto.LoginResponse;
import com.adfecomm.adfecomm.security.dto.UserDTO;
import com.adfecomm.adfecomm.security.jwt.JwtUtils;
import com.adfecomm.adfecomm.security.service.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OAuth2AuthenticationStrategy implements AuthenticationStrategy {

    private final JwtUtils jwtUtils;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public OAuth2AuthenticationStrategy(
            JwtUtils jwtUtils,
            OAuth2AuthorizedClientService authorizedClientService) {
        this.jwtUtils = jwtUtils;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public ResponseEntity<?> authenticate(LoginRequest loginRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Generate refresh token and store in cookie
        String refreshToken = jwtUtils.generateTokenFromEmail(userDetails);
        ResponseCookie refreshTokenCookie = jwtUtils.generateJwtCookie(refreshToken);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserDTO userDTO = new UserDTO(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );

        // Access token goes in response body
        String accessToken = jwtUtils.generateTokenFromEmail(userDetails);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new LoginResponse(userDTO, jwtUtils.getJwtExpirationMs()));
    }

    @Override
    public String extractToken(jakarta.servlet.http.HttpServletRequest request) {
        // First try access token from header
        String accessToken = jwtUtils.getJwtFromHeader(request);
        if (accessToken != null && jwtUtils.validateJwtToken(accessToken)) {
            return accessToken;
        }

        // If access token is invalid or missing, try refresh token from cookie
        return jwtUtils.getJwtFromCookies(request);
    }

    @Override
    public boolean supports(String protocol) {
        return "OAUTH2".equalsIgnoreCase(protocol);
    }
} 
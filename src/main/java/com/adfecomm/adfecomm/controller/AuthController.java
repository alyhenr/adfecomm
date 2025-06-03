package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.model.AppRole;
import com.adfecomm.adfecomm.model.Role;
import com.adfecomm.adfecomm.model.User;
import com.adfecomm.adfecomm.payload.APIResponse;
import com.adfecomm.adfecomm.payload.AuthResponseDTO;
import com.adfecomm.adfecomm.repository.RoleRepository;
import com.adfecomm.adfecomm.repository.UserRepository;
import com.adfecomm.adfecomm.security.auth.AuthenticationStrategy;
import com.adfecomm.adfecomm.security.auth.AuthenticationStrategyFactory;
import com.adfecomm.adfecomm.security.dto.SignUpRequest;
import com.adfecomm.adfecomm.security.jwt.JwtUtils;
import com.adfecomm.adfecomm.security.dto.LoginRequest;
import com.adfecomm.adfecomm.security.dto.LoginResponse;
import com.adfecomm.adfecomm.security.dto.UserDTO;
import com.adfecomm.adfecomm.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationStrategyFactory authenticationStrategyFactory;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Value("${spring.security.token.transport}")
    private String tokenTransport;
    
    @Value("${spring.security.auth.protocol}")
    private String authProtocol;

    private List<String> getUserRoles(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    /*
    If this authentication method is used then add jwtToken to LoginResponse properties
     */
    private LoginResponse generateLoginResponseJwtHeaders(Authentication authentication, UserDetailsImpl userDetails) {
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

//        return new LoginResponse(userDetails.getId(), userDetails.getUsername(), jwtToken, getUserRoles(userDetails));
        return new LoginResponse(new UserDTO(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), getUserRoles(userDetails)), jwtUtils.getJwtExpirationMs());
    }

    private LoginResponse generateLoginResponseJwtCookies(UserDetailsImpl userDetails) {
        return new LoginResponse(new UserDTO(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), getUserRoles(userDetails)), jwtUtils.getJwtExpirationMs());
    }

    private AuthResponseDTO generateAuthResponseDTO(UserDTO userDTO, UserDetailsImpl userDetails) {
        return new AuthResponseDTO(jwtUtils.getJwtExpirationMs(), userDTO);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        AuthenticationStrategy strategy = authenticationStrategyFactory.getStrategy(authProtocol);
        return strategy.authenticate(loginRequest, authentication);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new APIResponse("Error: Email is already in use!",false));
        }

        // Create new user's account
        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);
        
        userRepository.save(user);

        return ResponseEntity.ok(new APIResponse("User registered successfully!", true));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok()
                .body(new APIResponse("You've been signed out!", true));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUsername(Authentication authentication) {
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(
                    new LoginResponse(new UserDTO(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), getUserRoles(userDetails)), jwtUtils.getJwtExpirationMs()));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new APIResponse("Not logged in", false));
    }


}

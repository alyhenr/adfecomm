package com.adfecomm.adfecomm.controller;

import com.adfecomm.adfecomm.config.AppConstants;
import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.model.AppRole;
import com.adfecomm.adfecomm.model.Role;
import com.adfecomm.adfecomm.model.User;
import com.adfecomm.adfecomm.payload.APIResponse;
import com.adfecomm.adfecomm.repository.RoleRepository;
import com.adfecomm.adfecomm.repository.UserRepository;
import com.adfecomm.adfecomm.security.dto.SignUpRequest;
import com.adfecomm.adfecomm.security.jwt.JwtUtils;
import com.adfecomm.adfecomm.security.dto.LoginRequest;
import com.adfecomm.adfecomm.security.dto.LoginResponse;
import com.adfecomm.adfecomm.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;

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
        return new LoginResponse(userDetails.getId(), userDetails.getUsername(), getUserRoles(userDetails));
    }

    private LoginResponse generateLoginResponseJwtCookies(UserDetailsImpl userDetails) {
        return new LoginResponse(userDetails.getId(), userDetails.getUsername(), getUserRoles(userDetails));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail()
                            , loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        switch (AppConstants.AUTH_TYPE) {
            case "COOKIES":
                ResponseCookie jwtTokenCookie = jwtUtils.generateJwtCookie(userDetails); //From email currently
                LoginResponse response = generateLoginResponseJwtCookies(userDetails);
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtTokenCookie.toString())
                        .body(response);
        }

        return ResponseEntity.internalServerError().body("Failed to authenticate, please try again later");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new APIResponse("Email already in use", false));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new APIResponse("Email already in use", false));
        }

        User user = new User(signUpRequest.getEmail()
                , signUpRequest.getUsername()
                , encoder.encode(signUpRequest.getPassword()));

        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role for users not found"));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
        return ResponseEntity.ok(new APIResponse("User registered successfully", true));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUsername(Authentication authentication) {
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(
                    new LoginResponse(userDetails.getId()
                            , userDetails.getUsername()
                            , getUserRoles(userDetails)));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new APIResponse("Not logged in", false));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOutUser() {
        ResponseCookie cookie = jwtUtils.generateCleanCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new APIResponse("You've been signed out", true));

    }

}

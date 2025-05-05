package com.adfecomm.adfecomm.util;

import com.adfecomm.adfecomm.model.User;
import com.adfecomm.adfecomm.repository.UserRepository;
import com.adfecomm.adfecomm.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getEmail()) //Cookie generated with email currently, on JwtUtils
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String loggedInEmail() {
        return getAuthenticatedUser().getEmail();
    }

    public Long loggedInUserId() {
        return getAuthenticatedUser().getUserId();
    }

    public User loggedInUser() {
        return getAuthenticatedUser();
    }
}
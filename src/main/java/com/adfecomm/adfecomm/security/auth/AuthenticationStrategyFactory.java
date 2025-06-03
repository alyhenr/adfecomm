package com.adfecomm.adfecomm.security.auth;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationStrategyFactory {
    private final List<AuthenticationStrategy> strategies;

    public AuthenticationStrategyFactory(List<AuthenticationStrategy> strategies) {
        this.strategies = strategies;
    }

    public AuthenticationStrategy getStrategy(String protocol) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(protocol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported authentication protocol: " + protocol));
    }
} 
package com.pragma.powerup.infrastructure.security.config;

import org.springframework.stereotype.Component;

@Component
public class SecurityContext {
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static void setToken(String token) {
        tokenHolder.set(token);
    }

    public static String getToken() {
        return tokenHolder.get();
    }

    public static void clearToken() {
        tokenHolder.remove();
    }
}


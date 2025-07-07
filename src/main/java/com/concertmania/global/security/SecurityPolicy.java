package com.concertmania.global.security;

public class SecurityPolicy {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    public static final String[] ADMIN_ENDPOINTS = {
            "/api/admin/concerts",
            "/api/admin/locations"
    };
}

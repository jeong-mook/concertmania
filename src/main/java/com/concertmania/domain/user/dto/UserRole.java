package com.concertmania.domain.user.dto;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}

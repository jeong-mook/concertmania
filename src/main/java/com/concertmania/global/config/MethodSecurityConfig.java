package com.concertmania.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity // @PreAuthorize, @PostAuthorize 등 활성화
public class MethodSecurityConfig {
}
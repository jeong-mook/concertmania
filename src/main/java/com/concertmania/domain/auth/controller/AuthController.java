package com.concertmania.domain.auth.controller;


import com.concertmania.domain.auth.dto.AuthResponse;
import com.concertmania.domain.auth.dto.LoginRequest;
import com.concertmania.domain.auth.service.AuthService;
import com.concertmania.domain.user.dto.UserRole;
import com.concertmania.domain.user.dto.SignupRequest;
import com.concertmania.domain.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.register(request, UserRole.USER);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<AuthResponse> adminSignup
            (@Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.register(request, UserRole.ADMIN);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(Map.of("token", token));

    }






}

package com.concertmania.domain.auth.service;

import com.concertmania.domain.auth.dto.AuthResponse;
import com.concertmania.domain.user.dto.SignupRequest;
import com.concertmania.domain.user.dto.UserRole;
import com.concertmania.domain.user.model.User;
import com.concertmania.domain.user.repository.UserRepository;
import com.concertmania.global.error.ErrorCode;
import com.concertmania.global.error.exceptions.AuthException;
import com.concertmania.global.error.exceptions.EmailAlreadyExistsException;
import com.concertmania.global.security.JwtTokenProvider;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthException(ErrorCode.PASSWORD_MISMATCH);
        }

        return jwtTokenProvider.createToken(user.getEmail(), user.getUserRole().name());

    }

    public AuthResponse register(SignupRequest request, UserRole userRole) {
        if (userRepository.existsByEmail((request.email()))) {
            throw new EmailAlreadyExistsException();
        }

        User newUser = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .userRole(userRole)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        String token = jwtTokenProvider.createToken(newUser.getEmail(), newUser.getUserRole().name());

        return new AuthResponse(token);
    }



}

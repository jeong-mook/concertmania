package com.concertmania.domain.auth;

import com.concertmania.domain.user.dto.UserRole;
import com.concertmania.domain.user.model.User;
import com.concertmania.domain.user.repository.UserRepository;
import com.concertmania.global.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .name("테스트 유저")
                .userRole(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
        user.encodePassword(securityConfig.passwordEncoder());

        userRepository.save(user);
    }

    @Test
    void 로그인_성공_토큰_반환() throws Exception {
        LoginRequest login = new LoginRequest("test@example.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() throws Exception {
        LoginRequest login = new LoginRequest("test@example.com", "wrongPassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    static class LoginRequest {
        public String email;
        public String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}

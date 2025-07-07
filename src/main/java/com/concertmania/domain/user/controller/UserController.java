package com.concertmania.domain.user.controller;

import com.concertmania.domain.user.dto.UserDto;
import com.concertmania.domain.user.model.User;
import com.concertmania.domain.user.service.UserService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDto> dtoList = users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto dto) {
        User user = toEntity(dto);
        User saved = userService.registerUser(user);
        logger.info("User registered: id={}, email={}", saved.getId(), saved.getEmail());
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        logger.info("User deleted: id={}", id);
        return ResponseEntity.noContent().build();
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(null) // 보안상 비밀번호 미포함
                .name(user.getName())
                .userRole(user.getUserRole())
                .build();
    }

    private User toEntity(UserDto dto) {
        return new User(
                dto.getId(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getName(),
                dto.getUserRole(),
                null
        );
    }
}

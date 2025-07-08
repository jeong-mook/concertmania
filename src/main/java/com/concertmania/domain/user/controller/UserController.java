package com.concertmania.domain.user.controller;

import com.concertmania.domain.user.dto.UserDto;
import com.concertmania.domain.user.model.User;
import com.concertmania.domain.user.service.UserService;
import com.concertmania.global.dto.ApiResult;
import com.concertmania.global.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResult<PageResponse<UserDto.Response>>> getAllUsers() {
        Page<UserDto.Response> users = userService.findAllUsers();
        return ResponseEntity.ok(ApiResult.success(PageResponse.from(users)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = userService.findUserDtoById(id).map(UserDto.Response::from).orElseGet(UserDto.Response::empty);
        return ResponseEntity.ok(ApiResult.success(user));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto savedUser = userService.registerUser(userDto);
        log.info("User registered: id={}, email={}", savedUser.id(), savedUser.email());
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        log.info("User deleted: id={}", id);
        return ResponseEntity.noContent().build();
    }

}

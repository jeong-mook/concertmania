package com.concertmania.domain.user.service;

import com.concertmania.domain.user.dto.UserDto;
import com.concertmania.domain.user.dto.UserRole;
import com.concertmania.domain.user.model.User;
import com.concertmania.domain.user.repository.UserRepository;
import com.concertmania.global.error.exceptions.EmailAlreadyExistsException;
import com.concertmania.global.error.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // Spring Security PasswordEncoder 주입

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserDto.Response> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto.Response::from)
                .toList();
    }

    public Optional<User> findUserDtoById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public UserDto registerUser(UserDto userDto) {
        User user = new User(
                null,
                userDto.email(),
                userDto.password(),
                userDto.name(),
                userDto.userRole(),
                null
        );

        user.encodePassword(passwordEncoder);
        user.createdNow();

        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }




    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User registerUser(User user) {
        // 비밀번호 암호화
        user.encodePassword(passwordEncoder);
        user.createdNow();
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User registerUser(String email, String rawPassword, String name) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        User user = User.builder()
                .email(email)
                .password(rawPassword) // 평문 비밀번호 → 아래에서 암호화
                .name(name)
                .userRole(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        user.encodePassword(passwordEncoder); // 암호화 수행

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }
}

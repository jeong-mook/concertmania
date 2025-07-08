package com.concertmania.domain.user.dto;

import com.concertmania.domain.user.dto.UserRole;
import com.concertmania.domain.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(
        Long id,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일을 입력하세요.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @NotNull(message = "역할은 필수입니다.")
        UserRole userRole
) {
    public record Response(
            Long id,
            String email,
            String name,
            UserRole userRole
    ) {
        // 비밀번호를 제외한 DTO 생성을 위한 정적 팩토리 메서드
        public static UserDto from(User user) {
            return new UserDto(
                    user.getId(),
                    user.getEmail(),
                    null, // 보안상 비밀번호 미포함
                    user.getName(),
                    user.getUserRole()
            );
        }

        public static UserDto empty() {
            return new UserDto(null, null, null, null, null);
        }
    }
}


package com.concertmania.support;

import com.concertmania.domain.user.dto.UserRole;
import com.concertmania.domain.user.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // 테스트용 Principal 객체 생성
        User principal = User.builder()
                .id(1L) // 테스트에서는 보통 고정된 ID 사용
                .email(customUser.email())
                .userRole(UserRole.valueOf(customUser.role()))
                .build();

        // Authentication 객체 생성
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                "password", // credentials는 보통 사용되지 않으므로 아무 값이나 입력
                List.of(new SimpleGrantedAuthority("ROLE_" + customUser.role()))
        );

        context.setAuthentication(auth);
        return context;
    }
}


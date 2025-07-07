package com.concertmania.domain.queue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WaitingQueueDto {

    private Long id;

    @NotBlank(message = "세션 ID는 필수입니다.")
    private String sessionId;

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    private int queueNumber;

    private boolean entered;

    private LocalDateTime createdAt;
}

package com.concertmania.domain.concert.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateConcertRequestDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    private String title;

    @Size(max = 500, message = "설명은 최대 500자까지 입력 가능합니다.")
    private String description;

    @NotBlank(message = "장소는 필수 입력값입니다.")
    private Long locationId;

    @NotNull(message = "시작 시간은 필수 입력값입니다.")
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간은 필수 입력값입니다.")
    private LocalDateTime endTime;

    @NotNull(message = "예약 오픈 시간은 필수 입력값입니다.")
    private LocalDateTime reservationOpenAt;

    @NotNull(message = "예약 마감 시간은 필수 입력값입니다.")
    private LocalDateTime reservationCloseAt;
}

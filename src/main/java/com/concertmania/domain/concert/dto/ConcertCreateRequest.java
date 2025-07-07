package com.concertmania.domain.concert.dto;

import com.concertmania.global.util.validation.CompareFields;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@CompareFields(baseField = "startTime", matchField = "endTime", message = "종료일은 시작일보다 커야합니다.")
public record ConcertCreateRequest (
        @NotBlank String title,
        @NotBlank String description,
        Long locationId,

        @NotNull LocalDateTime concertDate,
        @NotNull LocalDateTime openAt
) {

}
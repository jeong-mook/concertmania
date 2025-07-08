package com.concertmania.domain.location.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeatGradeConfigRequest(
        @NotBlank String grade,
        @Min(1) int seatPerRow,
        @NotNull Character startRow,
        @NotNull Character endRow,
        @Min(0) int price
) {}

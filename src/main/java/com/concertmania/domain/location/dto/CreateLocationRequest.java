package com.concertmania.domain.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateLocationRequest(
        @NotBlank String name,
        @NotBlank String address,
        @NotNull List<SeatGradeConfigRequest> seatConfigs
) {
}

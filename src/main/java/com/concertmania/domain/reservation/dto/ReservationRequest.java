package com.concertmania.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        @NotNull Long seatId
) {
}

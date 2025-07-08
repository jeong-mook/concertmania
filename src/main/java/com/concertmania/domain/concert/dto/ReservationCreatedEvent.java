package com.concertmania.domain.concert.dto;

import java.math.BigDecimal;

public record ReservationCreatedEvent(
        Long reservationId,
        Long userId,
        BigDecimal amount
) {
}

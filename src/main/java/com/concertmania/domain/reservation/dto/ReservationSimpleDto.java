package com.concertmania.domain.reservation.dto;

import com.concertmania.domain.reservation.model.ReservationStatus;

public record ReservationSimpleDto(
        Long id,
        String concertName,
        String seatNumber,
        ReservationStatus status

) {}
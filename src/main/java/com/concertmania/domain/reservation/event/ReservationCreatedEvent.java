package com.concertmania.domain.reservation.event;


public record ReservationCreatedEvent(
        Long reservationId,
        Long userId,
        int amount
) {

}

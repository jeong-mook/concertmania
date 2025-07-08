package com.concertmania.domain.concert.dto;

import com.concertmania.domain.concert.model.Concert;
import com.concertmania.domain.concert.model.Seat;
import com.concertmania.domain.concert.model.SeatStatus;
import com.concertmania.domain.location.model.Location;
import com.concertmania.domain.location.model.LocationSeat;

import java.time.LocalDateTime;

public record SeatDto(
   Long id,
   String grade,
   SeatStatus status,
   Concert concert,
   LocationSeat locationSeat,
   Location location
) {

    public record Response(
            Long concertId,
            String concertName,
            String locationName,
            String locationAddress,
            LocalDateTime concertDate,
            LocalDateTime openAt,
            String grade,
            String seatNumber,
            int price
    ) {
        public static Response from (Seat seat) {
            return new Response(
                    seat.getConcert().getId(),
                    seat.getConcert().getName(),
                    seat.getConcert().getLocation().getName(),
                    seat.getConcert().getLocation().getLocation(),
                    seat.getConcert().getConcertDate(),
                    seat.getConcert().getOpenAt(),
                    seat.getGrade(),
                    seat.getSeatNumber(),
                    seat.getPrice()
            );
        }
    }
}

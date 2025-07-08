package com.concertmania.domain.reservation.dto;

import com.concertmania.domain.reservation.model.Reservation;
import com.concertmania.domain.reservation.model.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationDto {

    public record Request(
            Long seatId
    ) {}

    public record Response(
            Long reservationId,
            String concertName,
            String seatNumber,
            int price,
            ReservationStatus status,
            LocalDateTime createdAt
    ) {
        public static Response from (Reservation reservation) {
            return new Response(
                    reservation.getId(),
                    reservation.getConcert().getName(),
                    reservation.getSeat().getSeatNumber(),
                    reservation.getSeat().getPrice(),
                    reservation.getStatus(),
                    reservation.getReservedAt()
            );
        }
    }

    private Long id;

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "좌석 ID는 필수입니다.")
    private Long seatId;

    @NotNull(message = "콘서트 ID는 필수입니다.")
    private Long concertId;

    private LocalDateTime reservedAt;

    public static ReservationDto from(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .seatId(reservation.getSeat().getId())
                .concertId(reservation.getConcert().getId())
                .reservedAt(reservation.getReservedAt())
                .build();
    }
}

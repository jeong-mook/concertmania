package com.concertmania.domain.concert;

import com.concertmania.domain.concert.model.Concert;
import com.concertmania.domain.concert.model.Seat;
import com.concertmania.domain.concert.model.SeatStatus;
import com.concertmania.domain.reservation.model.Reservation;
import com.concertmania.domain.user.model.User;
import com.concertmania.domain.user.dto.UserRole;
import com.concertmania.domain.location.model.LocationSeat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ConcertTest {

    private User user;
    private Concert concert;
    private Seat avaliableSeat;
    private Seat reservedSeat;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test2@test.com")
                .userRole(UserRole.USER)
                .build();

        LocationSeat locationSeatInfo = LocationSeat.builder().price(150000).build();

        avaliableSeat = Seat.builder().id(1L).status(SeatStatus.AVAILABLE).locationSeat(locationSeatInfo).build();
        reservedSeat = Seat.builder().id(2L).status(SeatStatus.RESERVED).locationSeat(locationSeatInfo).build();

        concert = Concert.builder()
                .id(1L)
                .name("Test Concert")
                .seats(List.of(avaliableSeat, reservedSeat))
                .build();

        avaliableSeat.setConcert(concert);
        reservedSeat.setConcert(concert);
    }

    @Test
    @DisplayName("콘서트는 예약 가능한 좌석을 예약할 수 있다.")
    void reserve_available_seat_success() {
        // when
        Reservation reservation = concert.reserveSeat(user, avaliableSeat);

        // then
        assertThat(reservation).isNotNull();
        assertThat(reservation.getUser()).isEqualTo(user);
        assertThat(avaliableSeat.getStatus()).isEqualTo(SeatStatus.RESERVED);
    }

    @Test
    @DisplayName("이미 예약된 좌석을 예약하면 IllegalStateException 예외가 발생한다.")
    void reserve_reserved_seat_throws_exception() {
        // when & then
        assertThrows(IllegalStateException.class, () -> {
            concert.reserveSeat(user, reservedSeat);
        });
    }

}

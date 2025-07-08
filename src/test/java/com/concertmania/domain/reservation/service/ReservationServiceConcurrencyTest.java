package com.concertmania.domain.reservation.service;

import com.concertmania.domain.concert.model.Concert;
import com.concertmania.domain.concert.model.Seat;
import com.concertmania.domain.concert.model.SeatStatus;
import com.concertmania.domain.concert.repository.ConcertRepository;
import com.concertmania.domain.location.model.Location;
import com.concertmania.domain.location.model.LocationSeat;
import com.concertmania.domain.location.repository.LocationRepository;
import com.concertmania.domain.location.repository.LocationSeatRepository;
import com.concertmania.domain.reservation.repository.ReservationRepository;
import com.concertmania.domain.user.dto.UserRole;
import com.concertmania.domain.user.model.User;
import com.concertmania.domain.user.repository.UserRepository;
import com.concertmania.global.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ReservationServiceConcurrencyTest {

    @Autowired private ReservationService reservationService;
    @Autowired private UserRepository userRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private ConcertRepository concertRepository;
    @Autowired private LocationSeatRepository locationSeatRepository;
    @Autowired private SecurityConfig securityConfig;
    @Autowired private ReservationRepository reservationRepository;


    private User user;
    private Concert concert;
    private Seat seatToReserve;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .email("test@test.com")
                .password("1234")
                .name("테스트유저")
                .userRole(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
        user.encodePassword(securityConfig.passwordEncoder());
        userRepository.save(user);

        Location location = locationRepository.save(Location.builder().name("올림픽공원").location("서울시 강남구").build());

        LocationSeat locationSeat = locationSeatRepository.save(LocationSeat.builder().location(location).seatNumber("A1").grade("VIP").price(150000).build());

        concert = Concert.builder()
                .name("아이유 콘서트")
                .location(location)
                .concertDate(LocalDateTime.now().plusDays(30))
                .openAt(LocalDateTime.now().minusHours(1))
                .build();

        seatToReserve = Seat.builder()
                .concert(concert)
                .locationSeat(locationSeat)
                .status(SeatStatus.AVAILABLE)
                .build();

        concert.setSeats(List.of(seatToReserve));
        concertRepository.save(concert);

    }

    @Test
    @DisplayName("100명이 동시에 같은 좌석을 예약하면 1명만 성공해야 한다.")
    void when_100_users_reserve_same_seat_then_only_one_succeeds() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.reserveSeatInternal(user.getId(), concert.getId(), seatToReserve.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드의 작업이 끝날 때까지 대기
        executorService.shutdown();

        // then
        // 예약은 단 한 개만 생성되어야 한다.
        long reservationCount = reservationRepository.count();
        assertThat(reservationCount).isEqualTo(1);
    }

}

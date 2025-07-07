package com.concertmania.domain.reservation.controller;

import com.concertmania.domain.concert.model.Concert;
import com.concertmania.domain.concert.model.Seat;
import com.concertmania.domain.concert.model.SeatStatus;
import com.concertmania.domain.concert.repository.ConcertRepository;
import com.concertmania.domain.location.model.Location;
import com.concertmania.domain.location.model.LocationSeat;
import com.concertmania.domain.location.repository.LocationRepository;
import com.concertmania.domain.location.repository.LocationSeatRepository;
import com.concertmania.domain.reservation.dto.ReservationDto;
import com.concertmania.domain.user.dto.UserRole;
import com.concertmania.domain.user.model.User;
import com.concertmania.domain.user.repository.UserRepository;
import com.concertmania.global.config.SecurityConfig;
import com.concertmania.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReservationControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private ConcertRepository concertRepository;
    @Autowired private LocationSeatRepository locationSeatRepository;
    @Autowired private SecurityConfig securityConfig;

    private User user;
    private Concert concert;
    private Seat seatToReserve;

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
    @DisplayName("정상적인 좌석 예약 요청은 성공하고 예약 정보를 반환한다.")
    @WithMockCustomUser
    void reserveSeat_success() throws Exception {
        //given
        ReservationDto.Request requestDto = new ReservationDto.Request(seatToReserve.getId());
        String requestBody = objectMapper.writeValueAsString(requestDto);

        //when & then
        mockMvc.perform(post("/api/v1/concerts/{concertId}/reservations", concert.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.reservationId").exists())
                .andExpect(jsonPath("$.data.concertName").value("아이유 콘서트"))
                .andExpect(jsonPath("$.data.seatNumber").value("A1"));

    }


}

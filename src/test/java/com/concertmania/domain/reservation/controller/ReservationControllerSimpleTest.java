package com.concertmania.domain.reservation.controller;

import com.concertmania.domain.reservation.dto.ReservationDto;
import com.concertmania.domain.reservation.service.ReservationService;
import com.concertmania.support.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
public class ReservationControllerSimpleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean //TODO : 3.4.0 Deprecated . remove.. 대체 방법을 나중에 찾아보자.
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomUser
    @DisplayName("좌석 예약 API 슬라이스 테스트")
    void reserveSeat_slice_test() throws Exception {
        // given
        ReservationDto.Request requestDto = new ReservationDto.Request(1L);


        // when & then
        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1L))
                .andExpect(jsonPath("$.concertName").exists());


    }
}

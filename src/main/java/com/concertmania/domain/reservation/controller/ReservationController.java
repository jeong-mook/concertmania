package com.concertmania.domain.reservation.controller;

import com.concertmania.domain.reservation.dto.ReservationDto;
import com.concertmania.domain.reservation.service.ReservationService;
import com.concertmania.domain.user.model.User;
import com.concertmania.global.dto.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/concerts/{concertId}/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ApiResult<ReservationDto.Response>> reserveSet(
            @PathVariable Long concertId,
            @RequestBody ReservationDto.Request request,
            @AuthenticationPrincipal User user
            ) {
        Long userId = user.getId();

        ReservationDto.Response response = reservationService.reserveSeat(
                userId,
                concertId,
                request.seatId()
        );

        return ResponseEntity.ok(ApiResult.success(response));
    }
}

package com.concertmania.domain.concert.dto;

public record SeatGradeConfig(
        String grade, // 예 : "VIP", "R", "S"
        char startRow, // 시작 열 예 : 'A'
        char endRow, // 끝 열 예 : 'C'
        int seatPerRow // 각 행의 좌석 수
) {}

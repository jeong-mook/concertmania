package com.concertmania.domain.concert.model;

public enum SeatStatus {
    AVAILABLE, // 예약 가능
    RESERVED, // 점유중 (10분 제한)
    SOLD // 판매 완료
}

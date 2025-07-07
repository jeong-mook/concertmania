package com.concertmania.domain.location.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatGradeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grade;         // 좌석 등급 (예: VIP, R, S)
    private char startRow;        // 시작 행 (예: A)
    private char endRow;          // 끝 행 (예: D)
    private int seatPerRow;       // 행당 좌석 수
    private int price;            // 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}

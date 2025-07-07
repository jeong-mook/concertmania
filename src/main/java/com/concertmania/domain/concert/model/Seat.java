package com.concertmania.domain.concert.model;

import com.concertmania.domain.location.model.LocationSeat;
import com.concertmania.global.error.ErrorCode;
import com.concertmania.global.error.exceptions.ReservationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rowName; // 예: A, B, C...
    private int seatNumber; // 예: 1, 2, 3...

    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    @Setter
    private Concert concert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_seat_id", nullable = false)
    private LocationSeat locationSeat;


    public void makeReserved() {
        if (this.status != SeatStatus.AVAILABLE) {
            throw new ReservationException(ErrorCode.SEAT_ALREADY_RESERVED);
        }
        this.status = SeatStatus.RESERVED;
    }

    public void makeAvailable() {
        this.status = SeatStatus.AVAILABLE;
    }

    public int getPrice() {
        return this.locationSeat.getPrice();
    }

    public String getSeatNumber() {
        return this.locationSeat.getSeatNumber();
    }

    public void makeSold() {
        if (this.status != SeatStatus.AVAILABLE) {
            throw new ReservationException(ErrorCode.SEAT_ALREADY_RESERVED);
        }
        this.status = SeatStatus.SOLD;
    }

}
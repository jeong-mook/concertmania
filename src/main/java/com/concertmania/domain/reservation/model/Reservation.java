package com.concertmania.domain.reservation.model;

import com.concertmania.domain.concert.model.Concert;
import com.concertmania.domain.concert.model.Seat;
import com.concertmania.domain.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "IDX_RESERVATION_USER_ID", columnList = "user_id"),
        @Index(name = "IDX_RESERVATION_CONCERT_ID", columnList = "concert_id")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false, unique = true)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime reservedAt;
    private LocalDateTime expiredAt;

    private boolean canceled;

    @Builder
    public Reservation(User user, Seat seat, Concert concert, ReservationStatus status, LocalDateTime reservedAt, LocalDateTime expiredAt) {
        this.user = user;
        this.seat = seat;
        this.concert = concert;
        this.status = status;
        this.reservedAt = reservedAt;
        this.expiredAt = expiredAt;
    }

    public static Reservation create(User user, Concert concert, Seat seat) {
        return Reservation.builder()
                .user(user)
                .concert(concert)
                .seat(seat)
                .status(ReservationStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }
    public void cancel() {
        this.status = ReservationStatus.CANCELED;
    }




}

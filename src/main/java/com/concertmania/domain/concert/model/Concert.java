package com.concertmania.domain.concert.model;

import com.concertmania.domain.location.model.Location;
import com.concertmania.domain.reservation.model.Reservation;
import com.concertmania.domain.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false, unique = true)
    private Location location;

    @Column(nullable = false)
    private LocalDateTime concertDate;

    @Column(nullable = false)
    private LocalDateTime openAt;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();


    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    /**
     * @deprecated
     * 특정 좌성을 예약하고 예약 정보를 생성합니다.
     * @param userId
     * @param seatId
     * @return 생성된 Reservation 정보
     */
    public Reservation reserveSeat(Long userId, Long seatId) {
        Seat targetSeat = this.seats.stream()
                .filter(seat -> seat.getId().equals(seatId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("콘서트에 존재하지 않는 좌석입니다. ID: " + seatId));

        // Seat 객체에게 상태 변경을 '위임'합니다.
        targetSeat.makeReserved();

        // 예약(Reservation) 객체를 생성하여 반환합니다.
        return Reservation.create(User.builder().id(1L).build(), this, targetSeat);
    }

    /**
     * 특정 좌석을 예약하고 예약 정보를 생성합니다.
     * @param user 회원 모델
     * @param seat 좌석 모델
     * @return 생성된 Reservation 정보
     */
    public Reservation reserveSeat(User user, Seat seat) {
        seat.makeReserved();
        return Reservation.create(user, this, seat);
    }


    private Seat findSeat(Long seatId) {
        return this.seats.stream()
                .filter(s -> s.getId().equals(seatId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));
    }


}

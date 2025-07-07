package com.concertmania.domain.reservation.repository;

import com.concertmania.domain.reservation.model.Reservation;
import com.concertmania.domain.reservation.model.ReservationStatus;
import com.concertmania.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.seat WHERE r.seat.id = :seatId AND r.status = :status")
    Reservation findBySeatIdAndStatus(Long seatId, ReservationStatus status);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.seat JOIN FETCH r.user WHERE r.user = :user")
    Reservation findByUserWithSeatAndUser(@Param("user") User user);

}

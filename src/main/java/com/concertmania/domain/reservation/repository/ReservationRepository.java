package com.concertmania.domain.reservation.repository;

import com.concertmania.domain.reservation.dto.ReservationSimpleDto;
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

    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.concert WHERE r.id = :id")
    Optional<Reservation> findByIdWithConcert(@Param("id") Long id);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.concert c " +
            "JOIN FETCH r.seat s " +
            "JOIN FETCH s.locationSeat ls " +
            "WHERE r.user.id = :userId")
    List<Reservation> findAllByUserIdWithDetails(@Param("userId") Long userId);

    // JPQL의 new operation을 사용하여 즉시 DTO로 조회
    @Query("SELECT new com.concertmania.domain.reservation.dto.ReservationSimpleDto(r.id, c.name, ls.seatNumber, r.status) " +
            "FROM Reservation r " +
            "JOIN r.concert c " +
            "JOIN r.seat s " +
            "JOIN s.locationSeat ls " +
            "WHERE r.user.id = :userId")
    List<ReservationSimpleDto> findReservationSummariesByUserId(@Param("userId") Long userId);



}

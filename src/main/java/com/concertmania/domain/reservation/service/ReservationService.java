package com.concertmania.domain.reservation.service;

import com.concertmania.domain.concert.model.Concert;
import com.concertmania.domain.concert.model.Seat;
import com.concertmania.domain.concert.repository.ConcertRepository;
import com.concertmania.domain.reservation.dto.ReservationDto;
import com.concertmania.domain.reservation.event.ReservationCreatedEvent;
import com.concertmania.domain.reservation.model.Reservation;
import com.concertmania.domain.reservation.repository.ReservationRepository;
import com.concertmania.global.error.ErrorCode;
import com.concertmania.global.error.exceptions.ReservationException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReservationService(ReservationRepository reservationRepository, ConcertRepository concertRepository, ApplicationEventPublisher eventPublisher) {
        this.reservationRepository = reservationRepository;
        this.concertRepository = concertRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ReservationDto.Response reserveSeat(Long userId, Long concertId, Long seatId) {
        Concert concert = concertRepository.findByIdWithPessimisticLock(concertId)
                .orElseThrow(() -> new IllegalArgumentException("콘서트 정보를 찾을 수 없습니다."));

        Reservation reservation = concert.reserveSeat(userId, seatId);
        reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservationCreatedEvent(
                reservation.getId(),
                userId,
                reservation.getSeat().getPrice()
        ));

        return ReservationDto.Response.from(reservation);
    }

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void reservationSeatAvailable(Reservation reservation) {
        Seat seat = reservation.getSeat();
        Concert concert = seat.getConcert();
        concert.getSeats().remove(seat);
        seat.makeAvailable();
        concert.getSeats().add(seat);
        concertRepository.save(concert);
        reservationRepository.save(reservation);

    }

}

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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RedissonClient redissonClient;

    public ReservationService(ReservationRepository reservationRepository, ConcertRepository concertRepository, ApplicationEventPublisher eventPublisher, RedissonClient redissonClient) {
        this.reservationRepository = reservationRepository;
        this.concertRepository = concertRepository;
        this.eventPublisher = eventPublisher;
        this.redissonClient = redissonClient;
    }

    public ReservationDto.Response reserveSeat(Long userId, Long concertId, Long seatId) {
        // 사용자별로 고유한 락 키를 생성
        RLock lock = redissonClient.getLock("user-lock:" + userId);
        try {
            // 락 획득 시도 (최대 10초 대기, 획득 후 5초간 유지)
            boolean available = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!available) {
                throw new IllegalStateException("이미 다른 예약 요청을 처리 중입니다.");
            }

            // 락을 획득한 스레드만 비관적 락을 포함한 DB 트랜잭션 시작
            return reserveSeatInternal(userId, concertId, seatId);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 락 해제
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    @Transactional
    @CacheEvict(value = "concertSeats", key = "#concertId")
    public ReservationDto.Response reserveSeatInternal(Long userId, Long concertId, Long seatId) {
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

    @Transactional
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
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

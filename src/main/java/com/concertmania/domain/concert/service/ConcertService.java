package com.concertmania.domain.concert.service;

import com.concertmania.domain.concert.dto.ConcertDto;
import com.concertmania.domain.concert.dto.SeatDto;
import com.concertmania.domain.concert.dto.UpdateConcertRequestDto;
import com.concertmania.domain.concert.model.Concert;
import com.concertmania.domain.concert.repository.ConcertRepository;
import com.concertmania.domain.concert.repository.ConcertSpecifications;
import com.concertmania.domain.location.model.Location;
import com.concertmania.domain.location.repository.LocationRepository;
import com.concertmania.domain.reservation.dto.ReservationDto;
import com.concertmania.domain.reservation.model.Reservation;
import com.concertmania.domain.reservation.repository.ReservationRepository;
import com.concertmania.global.error.ErrorCode;
import com.concertmania.global.error.exceptions.ReservationException;
import com.concertmania.global.util.SpecBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final LocationRepository locationRepository;
    private final ReservationRepository reservationRepository;
    private final RestClient.Builder builder;
    private final ConcertSpecifications concertSpecifications;

    public ConcertService(
            ConcertRepository concertRepository,
            LocationRepository locationRepository, ReservationRepository reservationRepository,
            RestClient.Builder builder, ConcertSpecifications concertSpecifications) {
        this.concertRepository = concertRepository;
        this.locationRepository = locationRepository;
        this.reservationRepository = reservationRepository;
        this.builder = builder;
        this.concertSpecifications = concertSpecifications;
    }

    // TODO : 실험적으로 빌더 패턴을 이용하여 QueryDSL을 극단적으로 사용안하는 방법으로 해보았으나, 비효율적인 방법입니다.
    // TODO : 가독성이 갈수록 떨어질 것으로 보입니다. and (and 조건 하기 시작하면서 가독성이 최악으로 변경됩니다. 실험은 실험일 뿐 더이상 사용하지 않도록 하겠습니다.
    public Page<ConcertDto.Response> listConcerts(ConcertDto.Request request, Pageable pageable) {
        Specification<Concert> build = SpecBuilder.<Concert>create()
                .and(request.name() != null, concertSpecifications.nameContains(request.name()))
                .and(request.location() != null, (concertSpecifications.locationContains(request.location())))
                .and(request.openStartAt() != null && request.openEndAt() != null, (concertSpecifications.openBetweenAt(request.openStartAt(), request.openEndAt())))
                .and(request.openStartAt() != null && request.openEndAt() == null, concertSpecifications.concertStartAt(request.openStartAt()))
                .and(request.openEndAt() != null && request.openStartAt() == null, concertSpecifications.concertEndAt(request.openEndAt()))
                .and(request.concertStartDate() != null && request.concertEndDate() != null, (concertSpecifications.concertBeteween(request.concertStartDate(), request.concertEndDate())))
                .and(request.concertStartDate() != null && request.concertEndDate() == null, concertSpecifications.concertStartAt(request.concertStartDate()))
                .and(request.concertEndDate() != null && request.concertStartDate() == null, concertSpecifications.concertEndAt(request.concertEndDate()))
                .build();

        return concertRepository.findAll(build, pageable).map(ConcertDto.Response::from);
    }

    public Page<ConcertDto.Response> findAll(Pageable pageable) {
        return concertRepository.findAll(pageable).map(ConcertDto.Response::from);
    }

    public Optional<Concert> findById(Long id) {
        return concertRepository.findById(id);
    }

    @Cacheable(value = "concertSeats", key = "#concertId")
    public List<SeatDto.Response> findBySeat(Long concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new EntityNotFoundException("Concert not found"));

        return concert.getSeats().stream()
                .map(SeatDto.Response::from)
                .toList();
    }

    @Transactional
    public Concert save(Concert concert) {
        return concertRepository.save(concert);
    }

    @Transactional
    public void deleteById(Long id) {
        concertRepository.deleteById(id);
    }

    @Transactional
    public ConcertDto.Response createConcert(ConcertDto.CreateRequest request) {

        Concert concert = Concert.builder()
                .name(request.name())
                .location(request.location())
                .description(request.description())
                .concertDate(request.concertDate())
                .openAt(request.openAt())
                .createdAt(LocalDateTime.now())
                .build();

        concertRepository.save(concert);

        return ConcertDto.Response.from(concert);

    }

    @Transactional
    public ReservationDto reserveSeat(Long userId, Long concertId, Long seatId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new ReservationException(ErrorCode.CONCERT_NOT_FOUND));

        Reservation reservation = concert.reserveSeat(userId, seatId);

        reservationRepository.save(reservation);

        return ReservationDto.from(reservation);
    }

    @Transactional
    public Optional<Concert> updateConcert(Long id, UpdateConcertRequestDto updateRequest) {
        return concertRepository.findById(id)
                .map(concert -> {
                    Location location = locationRepository.findById(updateRequest.getLocationId())
                            .orElseThrow(() -> new EntityNotFoundException("Location not found"));

                    Concert updatedConcert = Concert.builder()
                            .id(concert.getId())
                            .name(updateRequest.getTitle())
                            .description(updateRequest.getDescription())
                            .location(location)
                            .concertDate(updateRequest.getStartTime())
                            .openAt(updateRequest.getReservationOpenAt())
                            .createdAt(concert.getCreatedAt())
                            .seats(concert.getSeats())
                            .build();

                    return concertRepository.save(updatedConcert);
                });
    }


}

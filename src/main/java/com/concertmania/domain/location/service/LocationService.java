package com.concertmania.domain.location.service;

import com.concertmania.domain.location.dto.CreateLocationRequest;
import com.concertmania.domain.location.model.Location;
import com.concertmania.domain.location.model.SeatGradeConfig;
import com.concertmania.domain.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    public void createLocationWithSeatGrades(CreateLocationRequest request) {
        Location location = Location.builder()
                .name(request.name())
                .location(request.address())
                .build();

        List<SeatGradeConfig> seatConfigs = request.seatConfigs().stream()
                .map(cfg -> SeatGradeConfig.builder()
                        .grade(cfg.grade())
                        .startRow(cfg.startRow())
                        .endRow(cfg.endRow())
                        .seatPerRow(cfg.seatPerRow())
                        .price(cfg.price())
                        .location(location) // 연관관계 주입
                        .build())
                .collect(Collectors.toList());

        location.getSeatConfigs().addAll(seatConfigs);

        locationRepository.save(location);
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location"));
    }
}

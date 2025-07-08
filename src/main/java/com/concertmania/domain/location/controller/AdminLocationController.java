package com.concertmania.domain.location.controller;

import com.concertmania.domain.location.dto.CreateLocationRequest;
import com.concertmania.domain.location.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/locations")
@RequiredArgsConstructor
public class AdminLocationController {

    private final LocationService locationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createLocation(@RequestBody @Valid CreateLocationRequest request) {
        locationService.createLocationWithSeatGrades(request);
        return ResponseEntity.ok().build();
    }
}

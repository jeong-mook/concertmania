package com.concertmania.domain.location.repository;

import com.concertmania.domain.location.model.LocationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationSeatRepository extends JpaRepository<LocationSeat, Long> {}

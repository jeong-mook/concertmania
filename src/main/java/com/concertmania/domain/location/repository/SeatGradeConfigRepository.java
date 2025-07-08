package com.concertmania.domain.location.repository;

import com.concertmania.domain.location.model.SeatGradeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatGradeConfigRepository extends JpaRepository<SeatGradeConfig, Long> {}

package com.concertmania.domain.queue.repository;

import com.concertmania.domain.queue.model.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaitingQueueRepository extends JpaRepository<WaitingQueue, Long> {
    Optional<WaitingQueue> findByUserId(Long userId);
}

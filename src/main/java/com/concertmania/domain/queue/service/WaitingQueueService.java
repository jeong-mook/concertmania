package com.concertmania.domain.queue.service;

import com.concertmania.domain.queue.model.WaitingQueue;
import com.concertmania.domain.queue.repository.WaitingQueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;

    public WaitingQueueService(WaitingQueueRepository waitingQueueRepository) {
        this.waitingQueueRepository = waitingQueueRepository;
    }

    public List<WaitingQueue> findAll() {
        return waitingQueueRepository.findAll();
    }

    public Optional<WaitingQueue> findById(Long id) {
        return waitingQueueRepository.findById(id);
    }

    @Transactional
    public WaitingQueue save(WaitingQueue waitingQueue) {
        return waitingQueueRepository.save(waitingQueue);
    }

    @Transactional
    public void deleteById(Long id) {
        waitingQueueRepository.deleteById(id);
    }

    public Optional<WaitingQueue> findByUserId(Long userId) {
        return waitingQueueRepository.findByUserId(userId);
    }
}

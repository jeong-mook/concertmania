package com.concertmania.domain.queue.controller;

import com.concertmania.domain.queue.dto.WaitingQueueDto;
import com.concertmania.domain.queue.model.WaitingQueue;
import com.concertmania.domain.queue.service.WaitingQueueService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/waiting-queue")
public class WaitingQueueController {

    private static final Logger logger = LoggerFactory.getLogger(WaitingQueueController.class);

    private final WaitingQueueService waitingQueueService;

    public WaitingQueueController(WaitingQueueService waitingQueueService) {
        this.waitingQueueService = waitingQueueService;
    }

    @GetMapping
    public ResponseEntity<List<WaitingQueueDto>> getAll() {
        List<WaitingQueue> list = waitingQueueService.findAll();
        List<WaitingQueueDto> dtoList = list.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaitingQueueDto> getById(@PathVariable Long id) {
        return waitingQueueService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WaitingQueueDto> create(@Valid @RequestBody WaitingQueueDto dto) {
        WaitingQueue entity = toEntity(dto);
        WaitingQueue saved = waitingQueueService.save(entity);
        logger.info("WaitingQueue created: id={}, sessionId={}", saved.getId(), saved.getToken());
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waitingQueueService.deleteById(id);
        logger.info("WaitingQueue deleted: id={}", id);
        return ResponseEntity.noContent().build();
    }

    private WaitingQueueDto toDto(WaitingQueue entity) {
        return WaitingQueueDto.builder()
                .id(entity.getId())
                .sessionId(entity.getToken())
                .userId(entity.getUser().getId())
                .createdAt(entity.getConcert().getCreatedAt())
                .build();
    }

    private WaitingQueue toEntity(WaitingQueueDto dto) {
        return WaitingQueue.builder()
                .token(dto.getSessionId())
                .build();
    }
}

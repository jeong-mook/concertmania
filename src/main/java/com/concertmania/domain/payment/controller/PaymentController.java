package com.concertmania.domain.payment.controller;

import com.concertmania.domain.payment.dto.PaymentDto;
import com.concertmania.domain.payment.model.Payment;
import com.concertmania.domain.payment.model.PaymentStatus;
import com.concertmania.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAll() {
        List<Payment> payments = paymentService.findAll();
        List<PaymentDto> dtoList = payments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PaymentDto> create(@Valid @RequestBody PaymentDto dto) {
        Payment payment = toEntity(dto);
        Payment saved = paymentService.save(payment);
        logger.info("Payment created: id={}, transactionId={}", saved.getId(), saved.getPgTransactionId());
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentDto> updateStatus(@PathVariable Long id, @RequestParam PaymentStatus status) {
        Payment updated = paymentService.updateStatus(id, status);
        logger.info("Payment status updated: id={}, status={}", updated.getId(), updated.getStatus());
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.deleteById(id);
        logger.info("Payment deleted: id={}", id);
        return ResponseEntity.noContent().build();
    }

    private PaymentDto toDto(Payment p) {
        return PaymentDto.builder()
                .id(p.getId())
                .reservationId(p.getReservation() != null ? p.getReservation().getId() : null)
                .transactionId(p.getPgTransactionId())
                .amount(p.getAmount())
                .status(p.getStatus())
                .paidAt(p.getPaidAt())
                .build();
    }

    private Payment toEntity(PaymentDto dto) {
//        Payment payment = new Payment();
//         Reservation 엔티티는 서비스 계층에서 조회 후 연결 권장
//        payment.setTransactionId(dto.getTransactionId());
//        payment.setAmount(dto.getAmount());
//        payment.setStatus(dto.getStatus());
//        payment.setPaidAt(dto.getPaidAt());
//        return payment;
        return new Payment(0L, null, BigDecimal.ONE, null, null, null, null);
    }
}

package com.concertmania.domain.concert.consumer;

import com.concertmania.domain.concert.dto.ReservationCreatedEvent;
import com.concertmania.domain.payment.model.Payment;
import com.concertmania.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSagaListener {

    private final PaymentService paymentService; // 외부 결제 처리 서비스

    @Async
    @TransactionalEventListener // 트랜잭션이 성공적으로 커밋된 후에만 이벤트 처리
    public void handleReservationCreated(ReservationCreatedEvent event) {
        try {
            // 1. 외부 결제 API 호출
            Payment payment = paymentService.processPayment(event.reservationId());
            log.debug("결제 성공 : {}", payment );
        } catch (Exception e) {
            // 2. 결제 실패 시 보상 트랜잭션(예약 취소) 호출
            paymentService.cancelReservation(event.reservationId());
        }
    }
}

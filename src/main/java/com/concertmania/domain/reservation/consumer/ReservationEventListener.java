package com.concertmania.domain.reservation.consumer;

import com.concertmania.domain.reservation.event.ReservationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    // private final PaymentService paymentService; // TODO 구현할 결제 서비스
    // private final NotificationService notificationService; // TODO 나중에 구현할 알림 서비스

    @Async
    @TransactionalEventListener
    public void handleReservationCreated(ReservationCreatedEvent event) {
        log.info("예약 생성 이벤트 수신! 예약 ID: {}", event.reservationId());

        try {
            // TODO : 결제 서비스 호출 로직 구현
            // paymentService.requestPayment(event.reservationId(), event.amount());
            log.info("결제 요청 완료. 예약 ID : {}", event.reservationId());
        } catch (Exception e) {
            log.error("결제 요청 실패. 보상 트랜잭션(예약 취소)을 시작해야 합니다.", e);
            // TODO : 결제 보상 트랜잭션 (SAGA) 트리거
            // eventPublisher.publishEvent(new PaymentFailedEvent(event.reservationId()));
        }

        //TODO : 사용자에게 이메일/SMS 알림 발송 로직
        // notificationService.sendSuccess(event.userId());
    }
}

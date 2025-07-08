package com.concertmania.domain.payment.consumer;

import com.concertmania.domain.payment.dto.PaymentFailedEventPayload;
import com.concertmania.domain.reservation.model.Reservation;
import com.concertmania.domain.reservation.service.ReservationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentFailedConsumer {

    private final ReservationService reservationService;

    public PaymentFailedConsumer(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @KafkaListener(topics = "payment-failed-topic", groupId = "concert-mania-group")
    public void handlePaymentFailed(PaymentFailedEventPayload payload) {
        Reservation reservation = reservationService.findById(payload.getReservationId());

        // 예약 상태 변경
        reservation.cancel();
        reservationService.save(reservation);

        // 좌석 상태 변경
        reservationService.reservationSeatAvailable(reservation);
    }
}

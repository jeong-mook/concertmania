package com.concertmania.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentFailedEventPayload {
    private Long paymentId;
    private Long reservationId;


    public PaymentFailedEventPayload(Long paymentId, Long reservationId) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
    }
}

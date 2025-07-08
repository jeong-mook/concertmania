package com.concertmania.domain.payment.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentFailedEvent extends ApplicationEvent {
    private final Long paymentId;

    public PaymentFailedEvent(Object source, Long paymentId) {
        super(source);
        this.paymentId = paymentId;
    }
}

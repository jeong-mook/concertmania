package com.concertmania.domain.payment.listener;

import com.concertmania.domain.payment.event.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {
    private static final Logger logger = LoggerFactory.getLogger(PaymentEventListener.class);

    @EventListener
    public void handlePaymentFailed(PaymentFailedEvent event) {
        logger.info("Payment failed event received for paymentId={}", event.getPaymentId());
        // 필요 시 추가 보상 로직 처리 가능 (예: 알림, 로그 등)
    }
}

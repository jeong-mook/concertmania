package com.concertmania.domain.payment.service;

import com.concertmania.domain.concert.service.SeatService;
import com.concertmania.domain.payment.dto.PaymentFailedEventPayload;
import com.concertmania.domain.payment.model.Payment;
import com.concertmania.domain.payment.model.PaymentStatus;
import com.concertmania.domain.payment.repository.PaymentRepository;
import com.concertmania.domain.reservation.service.ReservationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationService reservationService;
    private final SeatService seatService;
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTemplate<String, PaymentFailedEventPayload> kafkaTemplate;



    public PaymentService(PaymentRepository paymentRepository,
                          ReservationService reservationService,
                          SeatService seatService,
                          ApplicationEventPublisher eventPublisher,
                          KafkaTemplate<String, PaymentFailedEventPayload> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.reservationService = reservationService;
        this.seatService = seatService;
        this.eventPublisher = eventPublisher;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentRepository.findByPgTransactionId(transactionId);
    }

    @Transactional
    public Payment save(Payment payment) {
        payment.setPaidAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    @Transactional
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    @Transactional
    public Payment updateStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
        payment.updateStatus(status);
        return payment;
    }


    @Transactional
    public Payment processPayment(Long reservationId) {
         Payment payment = paymentRepository.findFirstByReservation_Id(reservationId);

         payment.setPaidAt(LocalDateTime.now());

         //TODO PG 실결제
         if (payment.getStatus().name().startsWith("FAIL")) {

             // 결제 실패 저장
             paymentRepository.save(payment);

             // 결제 실패 이벤트 발행
             PaymentFailedEventPayload payload = new PaymentFailedEventPayload(payment.getId(), payment.getReservation().getId());
             kafkaTemplate.send("payment-failed-topic", payload);

             return payment;

         }
        return paymentRepository.save(payment);

    }

    public void cancelReservation(Long reservationId) {
        Payment payment = paymentRepository.findFirstByReservation_Id(reservationId);

        if (payment.getStatus().name().startsWith("FAIL")) {
            // TODO : 결제 실패 전파 (MSA 환경 다른 서비스 전파 필수)
            PaymentFailedEventPayload payload = new PaymentFailedEventPayload(payment.getId(), payment.getReservation().getId());
            kafkaTemplate.send("payment-refund-topic", payload);

            //TODO PG 실결제 환불처리
        }

    }

}

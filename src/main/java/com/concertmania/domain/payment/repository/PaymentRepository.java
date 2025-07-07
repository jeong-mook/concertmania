package com.concertmania.domain.payment.repository;

import com.concertmania.domain.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPgTransactionId(String transactionId);

    Payment findFirstByReservation_Id(Long reservationId);
}

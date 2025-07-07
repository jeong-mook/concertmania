package com.concertmania.domain.payment.dto;

import com.concertmania.domain.payment.model.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentDto {

    private Long id;

    @NotNull(message = "예약 ID는 필수입니다.")
    private Long reservationId;

    private String transactionId;

    @Positive(message = "결제 금액은 양수여야 합니다.")
    private BigDecimal amount;

    @NotNull(message = "결제 상태는 필수입니다.")
    private PaymentStatus status;

    private LocalDateTime paidAt;
}

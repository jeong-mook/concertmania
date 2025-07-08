package com.concertmania.domain.payment.model;

public enum PaymentStatus {

    PENDING, COMPLETED, FAILED
    /**
     * TODO 외부 PG사의 다양한 실패/취소 사유를 구분하기 위해 상태값을 세분화
    SUCCESS,
    FAIL_INSUFFICIENT_FUNDS,
    FAIL_CARD_DECLINED,
    FAIL_TIMEOUT,
    FAIL_UNKNOWN,
    CANCEL_BY_USER,
    CANCEL_BY_SYSTEM
     */
}

package com.concertmania.global.error.exceptions;

import com.concertmania.global.error.BusinessException;
import com.concertmania.global.error.ErrorCode;

public class ReservationException extends BusinessException {
    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }
}

package com.concertmania.global.error.exceptions;

import com.concertmania.global.error.BusinessException;
import com.concertmania.global.error.ErrorCode;

public class AuthException extends BusinessException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}

package com.concertmania.global.error.exceptions;

import com.concertmania.global.error.BusinessException;
import com.concertmania.global.error.ErrorCode;

public class TokenExpiredException extends BusinessException {
    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED);
    }
}

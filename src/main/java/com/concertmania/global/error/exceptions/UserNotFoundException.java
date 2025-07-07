package com.concertmania.global.error.exceptions;

import com.concertmania.global.error.BusinessException;
import com.concertmania.global.error.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}

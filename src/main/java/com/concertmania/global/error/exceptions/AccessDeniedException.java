package com.concertmania.global.error.exceptions;

import com.concertmania.global.error.BusinessException;
import com.concertmania.global.error.ErrorCode;

public class AccessDeniedException extends BusinessException {
  public AccessDeniedException() {
    super(ErrorCode.ACCESS_DENIED);
  }
}

package com.concertmania.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;

    @Builder
    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, String customMessage) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.name())
                .message(customMessage)
                .build();
    }

}


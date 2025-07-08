package com.concertmania.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

    private final boolean success;
    private final T data;
    private final String message;

    public ApiResult(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }


    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, null);
    }

    public static ApiResult<?> error(String message) {
        return new ApiResult<>(false, null, message);
    }
}

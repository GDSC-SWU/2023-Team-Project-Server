package com.gdscswu_server.server.global.error.exception;

import com.gdscswu_server.server.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public ApiException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}

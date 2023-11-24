package com.gdscswu_server.server.domain.networking.error;

import com.gdscswu_server.server.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NetworkingErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String message;

    NetworkingErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
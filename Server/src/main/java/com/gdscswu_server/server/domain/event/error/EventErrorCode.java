package com.gdscswu_server.server.domain.event.error;

import com.gdscswu_server.server.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EventErrorCode implements ErrorCode {
    ;
    
    private final HttpStatus httpStatus;
    private final String message;

    EventErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

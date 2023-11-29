package com.gdscswu_server.server.domain.member.error;

import com.gdscswu_server.server.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements ErrorCode {
    ID_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "Id token required."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "Login failed."),
    INVALID_ID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid ID token."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    MemberErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

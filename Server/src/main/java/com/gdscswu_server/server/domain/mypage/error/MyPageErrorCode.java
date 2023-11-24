package com.gdscswu_server.server.domain.mypage.error;

import com.gdscswu_server.server.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MyPageErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String message;

    MyPageErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

package com.gdscswu_server.server.global.common;

import com.gdscswu_server.server.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ResponseDto {
    private final Integer code;
    private final String message;

    public static ResponseDto of(Integer code) {
        return new ResponseDto(code, HttpStatus.valueOf(code).getReasonPhrase());
    }

    public static ResponseDto of(Integer code, String message) {
        return new ResponseDto(code, message);
    }

    public static ResponseDto of(ErrorCode e) {
        return new ResponseDto(e.getHttpStatus().value(), e.getMessage());
    }
}

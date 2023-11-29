package com.gdscswu_server.server.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscswu_server.server.global.common.DataResponseDto;
import com.gdscswu_server.server.global.common.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseUtil {
    private final ObjectMapper mapper;

    public void setResponse(HttpServletResponse response, int status, String message) throws IOException {
        ResponseDto dto = ResponseDto.of(status, message);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(status);
        response.getWriter().write(mapper.writeValueAsString(dto));
    }

    public void setDataResponse(HttpServletResponse response, int status, Object data) throws IOException {
        ResponseDto dto = DataResponseDto.of(data, status);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(status);
        response.getWriter().write(mapper.writeValueAsString(dto));
    }
}

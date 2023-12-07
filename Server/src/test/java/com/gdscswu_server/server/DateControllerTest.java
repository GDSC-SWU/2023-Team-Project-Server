package com.gdscswu_server.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.gdscswu_server.server.domain.event.controller.DateController;
import com.gdscswu_server.server.domain.event.service.EventService;
import com.gdscswu_server.server.global.common.DataResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DateControllerTest {
    @Mock
    private EventService eventService;

    @InjectMocks
    private DateController dateController;

    @Test
    void getEventsByDate() {
        // 테스트 데이터 설정
        String date = "2023-01-01";
        List<String> mockEventNames = Arrays.asList("Event 1", "Event 2");

        // Mock 객체를 사용하여 Service의 동작을 설정
        when(eventService.getEventsByDate(date)).thenReturn(mockEventNames);

        // 테스트 수행
        ResponseEntity<DataResponseDto<List<String>>> response = dateController.getEventsByDate(date);

        // 결과 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEventNames, response.getBody().getData());

        // Service 메서드가 정확히 호출되었는지 검증
        verify(eventService, times(1)).getEventsByDate(date);
    }
}

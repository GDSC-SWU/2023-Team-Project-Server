package com.gdscswu_server.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.gdscswu_server.server.domain.event.domain.EventRepository;
import com.gdscswu_server.server.domain.event.service.EventService;
import com.gdscswu_server.server.domain.event.domain.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void getEventsByDate() {
        // 테스트 데이터 설정
        String date = "2023-01-01";
        LocalDate queryDate = LocalDate.parse(date);

        Event event1 = new Event();
        event1.setTitle("Event 1");

        Event event2 = new Event();
        event2.setTitle("Event 2");

        List<Event> mockEvents = Arrays.asList(event1, event2);

        // Mock 객체를 사용하여 Repository의 동작을 설정
        when(eventRepository.findByDateBetween(queryDate, queryDate)).thenReturn(mockEvents);

        // 테스트 수행
        List<String> result = eventService.getEventsByDate(date);

        // 결과 검증
        assertEquals(2, result.size());
        assertEquals("Event 1", result.get(0));
        assertEquals("Event 2", result.get(1));

        // Repository 메서드가 정확히 호출되었는지 검증
        verify(eventRepository, times(1)).findByDateBetween(queryDate, queryDate);
    }
}

package com.gdscswu_server.server.domain.event.service;

import org.springframework.stereotype.Service;
import com.gdscswu_server.server.domain.event.domain.Event;
import com.gdscswu_server.server.domain.event.domain.EventRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;

    public List<String> getEventsByDate(String date) {
        // 날짜 문자열을 LocalDate로 변환
        LocalDate queryDate = LocalDate.parse(date);

        // 날짜 범위에 해당하는 이벤트 조회
        List<Event> events = eventRepository.findByDateBetween(queryDate, queryDate);

        // 이벤트 리스트에서 이벤트 이름만 추출하여 반환
        return events.stream()
                .map(Event::getTitle)
                .collect(Collectors.toList());
    }
}

//

package com.gdscswu_server.server.domain.event.controller;

import com.gdscswu_server.server.domain.event.service.EventService;
import com.gdscswu_server.server.global.common.DataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService;

    // "/api/v1/event/by-date" 엔드포인트에 대한 GET 요청 처리
    @GetMapping("/by-date")
    public ResponseEntity<DataResponseDto<List<String>>> getEventsByDate(@RequestParam("date") String date) {
        // EventService를 사용하여 날짜에 해당하는 이벤트 이름 조회
        List<String> eventNames = eventService.getEventsByDate(date);

        // 조회 결과를 ResponseEntity로 래핑하여 반환
        return ResponseEntity.ok(DataResponseDto.of(eventNames, 200));
    }
}

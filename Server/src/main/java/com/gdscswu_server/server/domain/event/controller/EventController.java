package com.gdscswu_server.server.domain.event.controller;

import com.gdscswu_server.server.domain.event.service.AttendanceService;
import com.gdscswu_server.server.global.common.DataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
    private final AttendanceService attendanceService;
    @GetMapping("/{memberId}")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getMemberAttendanceStatus(@PathVariable Long memberId) {
        Map<String, List<Map<String, Object>>> attendanceStatus=attendanceService.getMemberAttendanceStatus(memberId);
        return ResponseEntity.ok(DataResponseDto.of(attendanceStatus, 200).getData());
    }
}

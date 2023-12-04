package com.gdscswu_server.server.domain.event.service;

import com.gdscswu_server.server.domain.event.domain.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<Map<String, Object>> getMemberAttendanceStatus(long memberId) {
        return attendanceRepository.getMemberAttendanceStatus(memberId);
    }

}

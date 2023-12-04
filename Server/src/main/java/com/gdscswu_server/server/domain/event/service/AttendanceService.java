package com.gdscswu_server.server.domain.event.service;

import com.gdscswu_server.server.domain.event.domain.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public Map<String, List<Map<String, Object>>> getMemberAttendanceStatus(long memberId) {
        List<Map<String, Object>> eventData = attendanceRepository.getMemberAttendanceStatus(memberId);

        Map<String, List<Map<String, Object>>> groupedData = groupDataByDate(eventData);

        return sortDataByDate(groupedData);
    }

    private static Map<String, List<Map<String, Object>>> groupDataByDate(List<Map<String, Object>> eventData) {
        Map<String, List<Map<String, Object>>> groupedData = new LinkedHashMap<>();

        for (Map<String, Object> event : eventData) {
            Date eventDate = (Date) event.get("event_date");

            // 날짜를 year-month 포맷으로 변환
            String formattedDate = formatDate(eventDate);

            groupedData.computeIfAbsent(formattedDate, k -> new ArrayList<>()).add(event);
        }

        return groupedData;
    }

    private static Map<String, List<Map<String, Object>>> sortDataByDate(Map<String, List<Map<String, Object>>> groupedData) {
        // 정렬된 Map으로 변환
        Map<String, List<Map<String, Object>>> sortedGroupedData = new LinkedHashMap<>();
        groupedData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedGroupedData.put(x.getKey(), x.getValue()));

        return sortedGroupedData;
    }

    private static String formatDate(Date date) {
        // 날짜를 year-month 포맷에 맞게 문자열로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        return dateFormat.format(date);
    }
}

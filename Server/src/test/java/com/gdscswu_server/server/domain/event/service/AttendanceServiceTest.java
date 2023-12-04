package com.gdscswu_server.server.domain.event.service;

import com.gdscswu_server.server.domain.event.domain.AttendanceRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Transactional
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMemberAttendanceStatus() {
        // Mocked 데이터 생성
        List<Map<String, Object>> mockedData = Collections.singletonList(
                Collections.singletonMap("event_id", 1L)
        );

        // Mock Repository 메서드 호출시 반환값 설정
        when(attendanceRepository.getMemberAttendanceStatus(anyLong())).thenReturn(mockedData);

        // Service 메서드 호출
        List<Map<String, Object>> result = attendanceService.getMemberAttendanceStatus(1L);

        // 예상 결과와 실제 결과 비교
        assertEquals(mockedData, result);
    }
}

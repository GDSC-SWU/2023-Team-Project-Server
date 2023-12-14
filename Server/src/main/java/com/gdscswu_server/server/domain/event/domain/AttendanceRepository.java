package com.gdscswu_server.server.domain.event.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query(nativeQuery = true, value = "SELECT e.id AS event_id, e.date AS event_date, e.title AS event_title, " +
            "CASE WHEN a.member_id IS NOT NULL THEN true ELSE false END AS attendance_status " +
            "FROM event e " +
            "LEFT JOIN attendance a ON e.id = a.event_id AND a.member_id = ?1")
    List<Map<String, Object>> getMemberAttendanceStatus(Long memberId);


}
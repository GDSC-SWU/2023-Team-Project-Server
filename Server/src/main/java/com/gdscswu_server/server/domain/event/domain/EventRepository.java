package com.gdscswu_server.server.domain.event.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // 날짜 범위에 해당하는 이벤트를 조회하는 메서드
    List<Event> findByDateBetween(LocalDate startDate, LocalDate endDate);
}

package com.gdscswu_server.server.domain.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenerationRepository extends JpaRepository<Generation, Long> {
    List<Generation> findAllByMemberOrderByNumberDesc(Member member);   // 기수 내림차순 적용
}

package com.gdscswu_server.server.domain.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByMemberAndGeneration(Member member, Generation generation);
    List<Project> findAllByMemberOrderByGenerationDesc(Member member);
}

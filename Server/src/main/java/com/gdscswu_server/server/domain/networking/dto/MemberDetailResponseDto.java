package com.gdscswu_server.server.domain.networking.dto;

import com.gdscswu_server.server.domain.member.domain.Generation;
import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.Project;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberDetailResponseDto {
    private Integer number; // 기수 (1기, 2기...)
    private String department; // 부서 (안드, UX/UI...)
    private String level; // 레벨 (코어, 리드...)
    private List<String> part; // 솔챌 파트 (피엠, 디자인...)

    public MemberDetailResponseDto(Generation generation, List<Project> project){
        this.number=generation.getNumber();
        this.department=generation.getDepartment();
        this.level=generation.getLevel();
        this.part=project.stream()
                .map(Project::getPart)
                .collect(Collectors.toList());
    }
}

package com.gdscswu_server.server.domain.networking.dto;

import com.gdscswu_server.server.domain.member.domain.Generation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GenerationResponseDto {
    private Integer number; // 기수 (1기, 2기...)
    private String department; // 부서 (안드, UX/UI...)
    private String level; // 레벨 (코어, 리드...)
    private List<ProjectResponseDto> projectList;

    @Builder
    public GenerationResponseDto(Generation generation, List<ProjectResponseDto> projectList) {
        this.number = generation.getNumber();
        this.department = generation.getDepartment();
        this.level = generation.getLevel();
        this.projectList = projectList;
    }

}

package com.gdscswu_server.server.domain.networking.dto;

import com.gdscswu_server.server.domain.member.domain.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectResponseDto {
    private String part; // 솔챌 파트 (피엠, 디자인...)
    @Builder
    public ProjectResponseDto(Project projects) {
        this.part=projects.getPart();
    }
}

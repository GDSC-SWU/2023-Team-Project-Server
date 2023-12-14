package com.gdscswu_server.server.domain.member.dto;

import com.gdscswu_server.server.domain.member.domain.Project;
import lombok.Getter;

@Getter
public class ProjectResponseDto {
    private Integer generation;
    private String title;
    private String part;

    public ProjectResponseDto(Project project) {
        this.generation = project.getGeneration().getNumber();
        this.title = project.getTitle();
        this.part = project.getPart();
    }
}

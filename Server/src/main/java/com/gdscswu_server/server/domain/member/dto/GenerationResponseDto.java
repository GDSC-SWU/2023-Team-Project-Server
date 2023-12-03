package com.gdscswu_server.server.domain.member.dto;

import com.gdscswu_server.server.domain.member.domain.Generation;
import lombok.Getter;

@Getter
public class GenerationResponseDto {
    private Integer number;
    private String department;
    private String level;

    public GenerationResponseDto(Generation generation) {
        this.number = generation.getNumber();
        this.department = generation.getDepartment();
        this.level = generation.getLevel();
    }
}

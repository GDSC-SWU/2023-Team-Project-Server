package com.gdscswu_server.server.domain.networking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FilterOptionsRequestDto {
    private String department;
    private String part;
    private String level;

    public FilterOptionsRequestDto(String departments, String parts, String levels) {
        this.department = departments;
        this.part = parts;
        this.level = levels;
    }
}

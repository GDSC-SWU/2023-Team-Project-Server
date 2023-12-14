package com.gdscswu_server.server.domain.networking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FilterOptionsRequestDto {
    private List<String> departments;
    private List<String> parts;
    private List<String> levels;

    @Builder
    public FilterOptionsRequestDto(List<String> departments, List<String> parts, List<String> levels) {
        this.departments = departments;
        this.parts = parts;
        this.levels = levels;
    }

    public List<String> getParts() {
        return parts != null ? parts : Collections.emptyList();
    }

    public List<String> getDepartments() {
        return departments != null ? departments : Collections.emptyList();
    }

    public List<String> getLevels() {
        return levels != null ? levels : Collections.emptyList();
    }
}

package com.gdscswu_server.server.domain.networking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberListUpdateRequestDto {
    private String department; // 부서 (안드, UX/UI...)
    private String level; // 레벨 (코어, 리드...)
    private String part; // 솔챌 파트 (피엠, 디자인...)

    @Builder
    public MemberListUpdateRequestDto(String department,String level,String part){
        this.department=department;
        this.level=level;
        this.part=part;
    }
}

package com.gdscswu_server.server.domain.networking.dto;

import com.gdscswu_server.server.domain.member.domain.Generation;
import com.gdscswu_server.server.domain.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberListResponseDto {
    private Long id;
    private String name; // 이름
    private String profileImagePath; // 프로필 이미지
    private String major; // 전공
    private Integer admissionYear; // 학번
    private Integer number; // 기수 (1기, 2기...)
    private String department; // 부서 (안드, UX/UI...)
    private String level; // 레벨 (코어, 리드...)
    private String part; // 파트 (피엠, 디자인...)


    public MemberListResponseDto(Generation generation){
        this.id=generation.getMember().getId();
        this.name=generation.getMember().getName();
        this.profileImagePath=generation.getMember().getProfileImagePath();
        this.major=generation.getMember().getMajor();
        this.admissionYear=generation.getMember().getAdmissionYear();
        this.number=generation.getNumber();
        this.department=generation.getDepartment();
        this.level=generation.getLevel();
        //this.part=generation.
    }
}

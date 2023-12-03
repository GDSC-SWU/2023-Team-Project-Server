package com.gdscswu_server.server.domain.member.dto;

import com.gdscswu_server.server.domain.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private String name;
    private String major;
    private Integer admissionYear;
    private String profileImagePath;
    private String introduction;
    private String email;

    public MemberResponseDto(Member member) {
        this.name = member.getName();
        this.major = member.getMajor();
        this.admissionYear = member.getAdmissionYear();
        this.profileImagePath = member.getProfileImagePath();
        this.introduction = member.getIntroduction();
        this.email = member.getEmail();
    }
}

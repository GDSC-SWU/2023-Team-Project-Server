package com.gdscswu_server.server.domain.member.dto;

import com.gdscswu_server.server.domain.member.domain.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberResponseDto {
    private String name;
    private String major;
    private Integer admissionYear;
    private String profileImagePath;
    private String introduction;
    private String email;
    private List<GenerationResponseDto> generationResponseDtoList;
    private List<ProjectResponseDto> projectResponseDtoList;

    public MemberResponseDto(Member member, List<GenerationResponseDto> generationResponseDtoList, List<ProjectResponseDto> projectResponseDtoList) {
        this.name = member.getName();
        this.major = member.getMajor();
        this.admissionYear = member.getAdmissionYear();
        this.profileImagePath = member.getProfileImagePath();
        this.introduction = member.getIntroduction();
        this.email = member.getEmail();
        this.generationResponseDtoList = generationResponseDtoList;
        this.projectResponseDtoList = projectResponseDtoList;
    }
}

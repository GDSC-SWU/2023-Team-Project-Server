package com.gdscswu_server.server.domain.networking.dto;

import com.gdscswu_server.server.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberResponseDto {
    private String name; // 이름
    private String major; // 학과
    private Integer admissionYear; // 학번
    private String profileImagePath; // 프로필 사진
    private boolean bookmark; // 북마크 저장 여부
    private List<GenerationResponseDto> generationResponseDtoList;

    @Builder
    public MemberResponseDto(Member member, boolean bookmark, List<GenerationResponseDto> generationResponseDtoList) {
        this.name = member.getName();
        this.profileImagePath = member.getProfileImagePath();
        this.major = member.getMajor();
        this.admissionYear = member.getAdmissionYear();
        this.bookmark = bookmark;
        this.generationResponseDtoList = generationResponseDtoList;
    }


}

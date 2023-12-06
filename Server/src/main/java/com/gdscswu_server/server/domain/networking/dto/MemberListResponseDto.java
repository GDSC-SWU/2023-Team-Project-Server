package com.gdscswu_server.server.domain.networking.dto;

import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.networking.domain.Bookmark;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberListResponseDto {
    private String name; // 이름
    private String major; // 학과
    private Integer admissionYear; // 학번
    private String profileImagePath; // 프로필 사진
    private boolean bookmark; // 북마크 저장 여부
    private List<MemberDetailResponseDto> memberDetails;

    @Builder
    public MemberListResponseDto(Member member, boolean bookmark, List<MemberDetailResponseDto> memberDetailResponseDtoList){

        this.name=member.getName();
        this.profileImagePath=member.getProfileImagePath();
        this.major=member.getMajor();
        this.admissionYear=member.getAdmissionYear();
        this.bookmark=bookmark;
        this.memberDetails=memberDetailResponseDtoList;
    }


}

package com.gdscswu_server.server.domain.member.dto;

import com.gdscswu_server.server.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileRequestDto {

    private String name;
    private String profileImagePath;
    private String major;
    private Integer admissionYear;
    private String introduction;
    private String email;

    @Builder
    public ProfileRequestDto(String name, String profileImagePath, String major, Integer admissionYear, String introduction, String email){
        this.name=name;
        this.profileImagePath=profileImagePath;
        this.major=major;
        this.admissionYear=admissionYear;
        this.introduction=introduction;
        this.email=email;
    }
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .profileImagePath(profileImagePath)
                .major(major)
                .admissionYear(admissionYear)
                .introduction(introduction)
                .email(email)
                .build();
    }
}
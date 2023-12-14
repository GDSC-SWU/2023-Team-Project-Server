package com.gdscswu_server.server.domain.member.dto;

import com.gdscswu_server.server.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileSaveRequestDto {  //프로필 정보를 저장할 dto

    private String name;
    private String major;
    private Integer admissionYear;
    private String introduction;
    private String email;

    @Builder
    public ProfileSaveRequestDto(String name, String major, Integer admissionYear, String introduction, String email) {
        this.name = name;
        this.major = major;
        this.admissionYear = admissionYear;
        this.introduction = introduction;
        this.email = email;
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .major(major)
                .admissionYear(admissionYear)
                .introduction(introduction)
                .email(email)
                .build();
    }
}
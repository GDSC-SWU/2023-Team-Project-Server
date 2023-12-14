package com.gdscswu_server.server.domain.member.domain;

import com.gdscswu_server.server.domain.member.dto.ProfileSaveRequestDto;
import com.gdscswu_server.server.domain.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@Entity
@Table(name = "Member")
@Getter
@NoArgsConstructor
@DynamicUpdate
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String googleEmail;

    private String name;
    private String profileImagePath;
    private String major;
    private Integer admissionYear;
    private String introduction;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String name, String major, Integer admissionYear, String introduction, String email) {
        this.name = name;
        this.major = major;
        this.admissionYear = admissionYear;
        this.introduction = introduction;
        this.email = email;

    }

    @Builder(builderMethodName = "googleBuilder")
    public Member(String googleEmail, String name, String profileImagePath) {
        this.googleEmail = googleEmail;
        this.name = name;
        this.profileImagePath = profileImagePath;
        this.role = Role.USER;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Member member)) return false;

        return Objects.equals(this.name, member.getName()) &&
                Objects.equals(this.email, member.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    public Member update(ProfileSaveRequestDto dto) {
        this.name = dto.getName();
        this.major = dto.getMajor();
        this.admissionYear = dto.getAdmissionYear();
        this.introduction = dto.getIntroduction();
        this.email = dto.getEmail();

        return this;
    }

    public void updateProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}
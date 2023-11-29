package com.gdscswu_server.server.domain.member.domain;

import com.gdscswu_server.server.domain.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "Member")
@Getter
@NoArgsConstructor
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

    public Member update(String name, String profileImagePath) {
        this.name = name;
        this.profileImagePath = profileImagePath;

        return this;
    }
}

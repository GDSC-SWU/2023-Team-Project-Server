package com.gdscswu_server.server.domain.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "Member")
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String email;
    private String major;
    private Integer admission_year;
    private String introduction;

    @Lob
    @Column
    private byte[] profile_image;

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
}

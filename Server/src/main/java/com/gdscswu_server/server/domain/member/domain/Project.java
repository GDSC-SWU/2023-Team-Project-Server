package com.gdscswu_server.server.domain.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "Project")
@Getter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Member member;

    @NotNull
    private String title;

    @NotNull
    private Integer generation;

    private String type;
    private String part;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Project project)) return false;

        return Objects.equals(this.id, project.getId()) &&
                Objects.equals(this.member, project.getMember()) &&
                Objects.equals(this.title, project.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, title);
    }
}

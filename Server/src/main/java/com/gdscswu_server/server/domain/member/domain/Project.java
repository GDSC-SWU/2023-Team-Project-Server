package com.gdscswu_server.server.domain.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "Project")
@Getter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Member member;

    @ManyToOne
    @JoinColumn(name = "generation_id")
    @NotNull
    private Generation generation;

    @NotNull
    private String title;

    private String type;
    private String part;

    @Builder
    public Project(Generation generation, Member member, String title, String part) {
        this.generation = generation;
        this.member = member;
        this.title = title;
        this.part = part;
    }

    @Builder
    public Project(Member member, String title, Generation generation) {
        this.member = member;
        this.title = title;
        this.generation = generation;
    }

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

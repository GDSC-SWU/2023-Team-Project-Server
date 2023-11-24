package com.gdscswu_server.server.domain.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "Generation")
@Getter
public class Generation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Member member;

    @NotNull
    private Integer number;

    @NotNull
    private String department;

    @NotNull
    private String level;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Generation generation)) return false;

        return Objects.equals(this.id, generation.getId()) &&
                Objects.equals(this.member, generation.getMember()) &&
                Objects.equals(this.number, generation.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, number);
    }
}

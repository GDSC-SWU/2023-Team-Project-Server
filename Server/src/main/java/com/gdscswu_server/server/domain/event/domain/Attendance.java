package com.gdscswu_server.server.domain.event.domain;

import com.gdscswu_server.server.domain.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "Attendance")
@Getter
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Member member;

    @ManyToOne
    @NotNull
    private Event event;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Attendance attendance)) return false;

        return Objects.equals(this.id, attendance.getId()) &&
                Objects.equals(this.member, attendance.getMember()) &&
                Objects.equals(this.event, attendance.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, event);
    }

    @Builder
    public Attendance(Member member, Event event) {
        this.member = member;
        this.event = event;
    }

}

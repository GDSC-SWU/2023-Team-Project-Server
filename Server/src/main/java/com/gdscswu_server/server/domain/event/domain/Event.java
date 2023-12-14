package com.gdscswu_server.server.domain.event.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Event")
@Getter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private LocalDate date;

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Event event)) return false;

        return Objects.equals(this.id, event.getId()) &&
                Objects.equals(this.title, event.getTitle()) &&
                Objects.equals(this.date, event.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, date);
    }

    @Builder
    public Event(String title, LocalDate date) {
        this.title = title;
        this.date = date;
    }
}

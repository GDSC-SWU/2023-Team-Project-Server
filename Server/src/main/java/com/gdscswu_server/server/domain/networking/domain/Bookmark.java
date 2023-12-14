package com.gdscswu_server.server.domain.networking.domain;

import com.gdscswu_server.server.domain.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Bookmark")
@Getter
@NoArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Member member;

    @ManyToOne
    @NotNull
    private Member targetMember;

    @CreatedDate
    private LocalDateTime publishedAt;

    @Builder
    public Bookmark(Member member, Member targetMember) {
        this.member = member;
        this.targetMember = targetMember;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Bookmark bookmark)) return false;

        return Objects.equals(this.id, bookmark.getId()) &&
                Objects.equals(this.member, bookmark.getMember()) &&
                Objects.equals(this.targetMember, bookmark.getTargetMember());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, targetMember);
    }
}

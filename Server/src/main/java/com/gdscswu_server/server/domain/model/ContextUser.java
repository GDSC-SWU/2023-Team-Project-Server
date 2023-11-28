package com.gdscswu_server.server.domain.model;

import com.gdscswu_server.server.domain.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class ContextUser extends User {
    private final Member member;

    public ContextUser(Member member) {
        super(member.getGoogleEmail(), member.getName(), Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())));
        this.member = member;
    }
}

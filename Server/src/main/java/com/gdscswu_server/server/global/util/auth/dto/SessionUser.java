package com.gdscswu_server.server.global.util.auth.dto;

import com.gdscswu_server.server.domain.member.domain.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String profile_image;

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.profile_image = member.getProfileImagePath();
    }
}

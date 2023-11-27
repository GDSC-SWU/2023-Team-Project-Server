package com.gdscswu_server.server.config.auth.dto;

import com.gdscswu_server.server.domain.member.domain.Member;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String profile_image;

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.profile_image = Arrays.toString(member.getProfile_image());
    }
}

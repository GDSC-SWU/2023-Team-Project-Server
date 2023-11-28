package com.gdscswu_server.server.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
}

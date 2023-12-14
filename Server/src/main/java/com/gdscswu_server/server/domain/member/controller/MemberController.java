package com.gdscswu_server.server.domain.member.controller;

import com.gdscswu_server.server.domain.member.dto.ProfileSaveRequestDto;
import com.gdscswu_server.server.domain.member.service.MemberService;
import com.gdscswu_server.server.domain.model.ContextUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping // 프로필 저장
    public Long profileSave(@AuthenticationPrincipal ContextUser contextUser,
                            @RequestPart MultipartFile profileImage,
                            @RequestPart("data") ProfileSaveRequestDto requestDto) throws IOException {
        return memberService.profileSave(contextUser.getMember(), profileImage, requestDto);
    }
}
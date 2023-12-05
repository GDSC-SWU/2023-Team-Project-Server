package com.gdscswu_server.server.domain.member.controller;

import com.gdscswu_server.server.domain.member.dto.ProfileSaveRequestDto;
import com.gdscswu_server.server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping // 프로필 저장
    public Long profileSave(@RequestBody ProfileSaveRequestDto requestDto){
        return memberService.profileSave(requestDto);
    }
}
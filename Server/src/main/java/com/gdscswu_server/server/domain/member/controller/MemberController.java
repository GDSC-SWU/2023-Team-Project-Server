package com.gdscswu_server.server.domain.member.controller;

import com.gdscswu_server.server.domain.member.dto.ProfileRequestDto;
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

    @PostMapping("/{id}") // 프로필 저장
    public Long profileSave(@RequestBody ProfileRequestDto requestDto){
        return memberService.profileSave(requestDto);
    }

    @PutMapping("/update/{id}") // 프로필 수정
    public Long profileUpdate(@PathVariable Long id, @RequestBody ProfileRequestDto requestDto){
        return memberService.profileUpdate(id, requestDto);
    }
}
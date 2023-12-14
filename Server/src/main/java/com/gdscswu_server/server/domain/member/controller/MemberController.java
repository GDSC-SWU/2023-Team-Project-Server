package com.gdscswu_server.server.domain.member.controller;

import com.gdscswu_server.server.domain.member.dto.ProfileSaveRequestDto;
import com.gdscswu_server.server.domain.model.ContextUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import com.gdscswu_server.server.domain.member.dto.MemberResponseDto;
import com.gdscswu_server.server.domain.member.service.MemberService;
import com.gdscswu_server.server.global.common.DataResponseDto;
import com.gdscswu_server.server.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                            @RequestPart(required = false) MultipartFile profileImage,
                            @RequestPart("data") ProfileSaveRequestDto requestDto) throws IOException {
        return memberService.profileSave(contextUser.getMember(), profileImage, requestDto);
    }
  
    @GetMapping("/test")
    public ResponseEntity<ResponseDto> test() {
        return ResponseEntity.ok(DataResponseDto.of("hello world", 200));
    }

    @GetMapping("/{id}")
    public MemberResponseDto findById(@PathVariable Long id) {
        return memberService.findById(id);
    }
}

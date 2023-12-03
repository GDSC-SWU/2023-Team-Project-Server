package com.gdscswu_server.server.domain.member.controller;

import com.gdscswu_server.server.domain.member.dto.GenerationResponseDto;
import com.gdscswu_server.server.domain.member.dto.MemberResponseDto;
import com.gdscswu_server.server.domain.member.dto.ProjectResponseDto;
import com.gdscswu_server.server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    /*
    <참고용 예시 코드> spring context에 저장된 member entity 가져오기
    @GetMapping
    public ResponseEntity<ResponseDto> test(@AuthenticationPrincipal ContextUser contextUser) {
        Member member = contextUser.getMember();

        return ResponseEntity.ok(DataResponseDto.of(member, 200));
    }
     */

    @GetMapping("/{id}")
    public MemberResponseDto findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @GetMapping("/generation/{id}")
    public List<GenerationResponseDto> findGenerationByMemberId(@PathVariable Long id) {
        return memberService.findGenerationByMemberId(id);
    }

    @GetMapping("/project/{id}")
    public List<ProjectResponseDto> findProjectByMemberId(@PathVariable Long id) {
        return memberService.findProjectByMemberId(id);
    }
}

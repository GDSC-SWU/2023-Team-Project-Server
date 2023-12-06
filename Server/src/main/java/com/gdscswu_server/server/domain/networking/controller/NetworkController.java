package com.gdscswu_server.server.domain.networking.controller;

import com.gdscswu_server.server.domain.networking.dto.FilterOptionsRequestDto;
import com.gdscswu_server.server.domain.networking.dto.MemberResponseDto;
import com.gdscswu_server.server.domain.networking.service.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/network")
public class NetworkController {
    private final NetworkService networkService;

    // 멤버 리스트 응답
    @GetMapping("")
    public List<MemberResponseDto> getAllMembers() {
        return networkService.findAllMembers();
    }

    // 북마크 설정
    @PostMapping("/bookmark/{memberId}")
    public List<MemberResponseDto> setBookmark(@PathVariable Long memberId){
        return networkService.bookmarkMember(memberId);
    }

}

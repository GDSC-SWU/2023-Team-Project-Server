package com.gdscswu_server.server.domain.networking.controller;

import com.gdscswu_server.server.domain.networking.dto.FilterOptionsRequestDto;
import com.gdscswu_server.server.domain.networking.dto.MemberResponseDto;
import com.gdscswu_server.server.domain.networking.service.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/network")
public class NetworkController {
    private final NetworkService networkService;

    // 필터링 거쳐서 멤버 리스트 응답
    // 필터링 할 filterCondition=null 이면 전체 멤버 리스트 return
    // null 아니면 필터링 된 일부 멤버 리스트 return
    @PostMapping
    public ResponseEntity<Object> getAllMembers(@RequestBody FilterOptionsRequestDto filterOptionsRequestDto) {
        return networkService.findAllMembers(filterOptionsRequestDto);
    }

    // 북마크 설정/해제
    @PostMapping("/bookmark/{memberId}")
    public ResponseEntity<Object> setBookmark(@PathVariable Long memberId){
        return networkService.bookmarkMember(memberId);
    }

}

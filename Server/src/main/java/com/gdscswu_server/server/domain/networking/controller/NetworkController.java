package com.gdscswu_server.server.domain.networking.controller;

import com.gdscswu_server.server.domain.networking.dto.MemberResponseDto;
import com.gdscswu_server.server.domain.networking.service.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    // 특정 조건 멤버 리스트 응답


}

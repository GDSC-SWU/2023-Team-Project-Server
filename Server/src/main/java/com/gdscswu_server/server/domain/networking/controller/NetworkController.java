package com.gdscswu_server.server.domain.networking.controller;

import com.gdscswu_server.server.domain.networking.service.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/network")
public class NetworkController {
    private final NetworkService networkService;

    // 멤버 리스트 응답
    @GetMapping("")
    public ResponseEntity<Object> getAllMembers() {
        return networkService.findAllMembers();
    }


}

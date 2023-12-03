package com.gdscswu_server.server.domain.networking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscswu_server.server.domain.member.domain.Generation;
import com.gdscswu_server.server.domain.member.domain.GenerationRepository;
import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.model.Role;
import com.gdscswu_server.server.domain.networking.dto.MemberListResponseDto;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.access.SecurityConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class NetworkControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private GenerationRepository generationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("전체 멤버 조회 테스트")
    public void FindAllMembers() throws Exception{
        // Given
        Member savedMember = memberRepository.save(Member.builder()
                .googleEmail("abc@abc.com")
                .name("memberName")
                .profileImagePath("path")
                .build());

        List<Generation> memberList = IntStream.range(1,31)
                .mapToObj(i ->
                        Generation.builder()
                                .member(savedMember)
                                .number(1+i)
                                .department("server"+i)
                                .level("core"+i)
                                .build()
                )
                .collect(Collectors.toList());

        // when
        generationRepository.saveAll(memberList);

        // then
        String url="http://localhost:" + port + "/api/v1/network";
        mockMvc.perform(get(url)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("&[0].number").value(1));

    }
}
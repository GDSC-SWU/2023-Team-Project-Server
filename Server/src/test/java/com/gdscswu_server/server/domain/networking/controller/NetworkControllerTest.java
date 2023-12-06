package com.gdscswu_server.server.domain.networking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscswu_server.server.domain.member.domain.*;
import com.gdscswu_server.server.domain.member.dto.LoginResponseDto;
import com.gdscswu_server.server.domain.model.Role;
import com.gdscswu_server.server.domain.networking.dto.MemberDetailResponseDto;
import com.gdscswu_server.server.domain.networking.dto.MemberListResponseDto;
import com.gdscswu_server.server.global.util.JwtUtil;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private ProjectRepository projectRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;
    @Test
    @DisplayName("전체 멤버 조회 테스트")
    public void FindAllMembers() throws Exception{
        // Given
        Member member = memberRepository.save(Member.builder()
                .googleEmail("abc@abc.com")
                .name("memberName")
                .profileImagePath("path")
                .build());

        Project project1 = projectRepository.save(Project.builder()
                .title("프젝 제목")
                .member(member)
                .part("PM")
                .build());

        Project project2 = projectRepository.save(Project.builder()
                .title("프젝 제목")
                .member(member)
                .part("Android")
                .build());

        Generation generation=generationRepository.save(Generation.builder()
                .member(member)
                .number(1)
                .department("Server")
                .level("Member")
                .build());

        MemberDetailResponseDto memberDetailResponseDto = MemberDetailResponseDto.builder()
                .generation(generation)
                .projects(List.of(project1, project2))
                .build();

        MemberListResponseDto memberListResponseDto=MemberListResponseDto.builder()
                .member(member)
                .bookmark(false)
                .memberDetailResponseDtoList(List.of(memberDetailResponseDto))
                .build();

        // When
        generationRepository.saveAll(List.of(generation));
        projectRepository.saveAll(List.of(project1, project2));

        // then
        String url="http://localhost:" + port + "/api/v1/network";
        LoginResponseDto loginResponseDto = jwtUtil.generateTokens(member);

        mockMvc.perform(get(url)
                        .contentType(APPLICATION_JSON).header("Authorization", "Bearer " + loginResponseDto.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[0].memberDetails[0].number").value(1))
                .andExpect(jsonPath("$[0].memberDetails[0].part").isArray())
                .andExpect(jsonPath("$[0].memberDetails[0].part").isNotEmpty())
                .andDo(print());

    }
}
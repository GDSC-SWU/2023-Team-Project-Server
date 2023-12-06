package com.gdscswu_server.server.domain.networking.controller;

import com.gdscswu_server.server.domain.member.domain.*;
import com.gdscswu_server.server.domain.member.dto.LoginResponseDto;
import com.gdscswu_server.server.domain.networking.dto.GenerationResponseDto;
import com.gdscswu_server.server.domain.networking.dto.MemberResponseDto;
import com.gdscswu_server.server.domain.networking.dto.ProjectResponseDto;
import com.gdscswu_server.server.global.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    public void findAllMembers() throws Exception{
        // Given
        Member member1 = memberRepository.save(Member.builder()
                .googleEmail("abc@abc.com")
                .name("김슈니")
                .profileImagePath("path")
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .googleEmail("def@def.com")
                .name("박슈니")
                .profileImagePath("path")
                .build());

        Project project1 = projectRepository.save(Project.builder()
                .title("김슈니의 1기 첫번째 프젝")
                .member(member1)
                .part("PM")
                .build());

        Project project2 = projectRepository.save(Project.builder()
                .title("김슈니의 1기 첫번째 프젝")
                .member(member1)
                .part("Front")
                .build());

        Project project3 = projectRepository.save(Project.builder()
                .title("김슈니의 1기 두번째 프젝")
                .member(member1)
                .part("Front")
                .build());

        Project project4 = projectRepository.save(Project.builder()
                .title("김슈니의 2기 첫번째 프젝")
                .member(member1)
                .part("Back")
                .build());

        Project project5 = projectRepository.save(Project.builder()
                .title("박슈니의 2기 첫번째 프젝")
                .member(member2)
                .part("Back")
                .build());

        Generation generation1=generationRepository.save(Generation.builder()
                .member(member1)
                .number(1)
                .department("Android")
                .level("Member")
                .build());

        Generation generation2=generationRepository.save(Generation.builder()
                .member(member1)
                .number(2)
                .department("Server")
                .level("Core")
                .build());

        Generation generation3=generationRepository.save(Generation.builder()
                .member(member2)
                .number(2)
                .department("Server")
                .level("Member")
                .build());

        ProjectResponseDto partListResponseDto1 = ProjectResponseDto.builder()
                .projects(project1)
                .build();
        ProjectResponseDto partListResponseDto2 = ProjectResponseDto.builder()
                .projects(project2)
                .build();
        ProjectResponseDto partListResponseDto3 = ProjectResponseDto.builder()
                .projects(project3)
                .build();
        ProjectResponseDto partListResponseDto4 = ProjectResponseDto.builder()
                .projects(project4)
                .build();
        ProjectResponseDto partListResponseDto5 = ProjectResponseDto.builder()
                .projects(project5)
                .build();

        GenerationResponseDto generationResponseDto1 = GenerationResponseDto.builder()
                .generation(generation1)
                .build();

        GenerationResponseDto generationResponseDto2 = GenerationResponseDto.builder()
                .generation(generation2)
                .build();

        GenerationResponseDto generationResponseDto3 = GenerationResponseDto.builder()
                .generation(generation3)
                .build();

        MemberResponseDto memberResponseDto1 = MemberResponseDto.builder()
                .member(member1)
                .bookmark(false)
                .generationResponseDtoList(List.of(generationResponseDto1,generationResponseDto2))
                .projectResponseDtoList(List.of(partListResponseDto1,partListResponseDto2,partListResponseDto3,partListResponseDto4))
                .build();

        MemberResponseDto memberResponseDto2 = MemberResponseDto.builder()
                .member(member2)
                .bookmark(true)
                .generationResponseDtoList(List.of(generationResponseDto3))
                .projectResponseDtoList(List.of(partListResponseDto5))
                .build();

        // When
        generationRepository.saveAll(List.of(generation1, generation2, generation3));
        projectRepository.saveAll(List.of(project1, project2, project3, project4,project5));

        // then
        String url = "http://localhost:" + port + "/api/v1/network";
        LoginResponseDto loginResponseDto = jwtUtil.generateTokens(member1);

        mockMvc.perform(get(url)
                        .contentType(APPLICATION_JSON).header("Authorization", "Bearer " + loginResponseDto.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value("김슈니"))
                //.andExpect(jsonPath("$[1].memberDetails[0].partList[0].part[0]").value("PM"))
                //.andExpect(jsonPath("$[1].memberDetails[0].partList[1].part[0]").value("Front"))
                .andDo(print());

    }
}
package com.gdscswu_server.server.domain.Member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscswu_server.server.domain.member.domain.*;
import com.gdscswu_server.server.domain.member.dto.ProfileSaveRequestDto;
import com.gdscswu_server.server.domain.model.TokenClaimVo;
import com.gdscswu_server.server.global.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
@Transactional
public class MemberControllerTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GenerationRepository generationRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("findById() Test")
    public void findById() throws Exception {
        // 정보를 보고 싶은 멤버 생성
        Member member = new Member("googleEmail", "name", "profileImagePath");
        memberRepository.save(member);

        // 정보를 보고 싶은 멤버의 기수 생성
        Generation generation = Generation.builder()
                .member(member)
                .number(1)
                .department("Server")
                .level("Member")
                .build();
        generationRepository.save(generation);

        // 정보를 보고 싶은 멤버의 두번째 기수 생성
        Generation generation2 = Generation.builder()
                .member(member)
                .number(2)
                .department("Server")
                .level("Core Member")
                .build();
        generationRepository.save(generation2);

        // 정보를 보고 싶은 멤버의 프로젝트 생성
        Project project = Project.builder()
                .member(member)
                .title("project")
                .generation(generation)
                .build();
        projectRepository.save(project);

        // 정보를 보고 싶은 멤버의 두번째 프로젝트 생성
        Project project1 = Project.builder()
                .member(member)
                .title("project2")
                .generation(generation2)
                .build();
        projectRepository.save(project1);

        // 정보를 보려는 멤버 생성
        Member member2 = new Member("googleEmail", "name", "profileImagePath");
        memberRepository.save(member2);

        // 정보를 보려는 멤버의 액세스 토큰을 생성
        TokenClaimVo vo = new TokenClaimVo(member2.getId(), member2.getEmail());
        String accessToken = jwtUtil.generateToken(true, vo);

        // mockMvc 요청 구성
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/member/{id}", member.getId())
                .header("Authorization", "Bearer " + accessToken);  // 위에서 생성한 액세스 토큰을 헤더에 추가

        // mockMvc 실행 및 결과 확인
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("profileSave() 테스트")
    public void profileSave() throws Exception {

//        Member member = Member.builder()
//                .name("lee")
//                .major("major")
//                .admissionYear(11111)
//                .introduction("hi")
//                .email("aaaaa@com")
//                .build();

        Member member = new Member("aaaaa@gmail.com", "lee", null);

        Long memberId = memberRepository.save(member).getId();

        ProfileSaveRequestDto requestDto = ProfileSaveRequestDto.builder()
                .name("lee")
                .major("major")
                .admissionYear(11111)
                .introduction("hi")
                .email("aaaaa@com")
                .build();

        MockMultipartFile file = new MockMultipartFile("profileImage", "test.png", "image/png", new ClassPathResource("test.png").getInputStream());
        MockMultipartFile json = new MockMultipartFile("data", "", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8));
//
//        String content = objectMapper.writeValueAsString(requestDto);
//
        String accessToken = jwtUtil.generateTokens(member).getAccessToken();

        mockMvc.perform(multipart("/api/v1/member")
                        .file(file)
                        .file(json)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());

        Member member1 = memberRepository.findById(memberId).orElseThrow(() -> new Exception("null"));
        assertThat(member1.getProfileImagePath()).as("Profile image path가 수정되지 않음.").isNotNull();


//        mockMvc.perform(post("/api/v1/member")
//                        .content(content)
//                        .contentType(APPLICATION_JSON)
//                        .accept(APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andDo(print());

    }

    @Test
    @DisplayName("Profile image 삭제 테스트")
    public void removeProfileImage() throws Exception {
        // given
        Member member = new Member("aaaaa@gmail.com", "lee", null);
        Long memberId = memberRepository.save(member).getId();
        String accessToken = jwtUtil.generateTokens(member).getAccessToken();
        ProfileSaveRequestDto requestDto = ProfileSaveRequestDto.builder()
                .name("lee")
                .major("major")
                .admissionYear(11111)
                .introduction("hi")
                .email("aaaaa@com")
                .build();

        MockMultipartFile json = new MockMultipartFile("data", "", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8));

        // when
        ResultActions resultActions = mockMvc.perform(multipart("/api/v1/member")
                .file(json)
                .header("Authorization", "Bearer " + accessToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
        Member member1 = memberRepository.findById(memberId).orElseThrow(() -> new Exception("null"));
        assertThat(member1.getProfileImagePath()).as("Profile image path가 수정되지 않음.").isNull();
    }
}
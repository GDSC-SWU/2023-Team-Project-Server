package com.gdscswu_server.server.domain.member.controller;

import com.gdscswu_server.server.domain.member.domain.Generation;
import com.gdscswu_server.server.domain.member.domain.GenerationRepository;
import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.model.TokenClaimVo;
import com.gdscswu_server.server.global.util.JwtUtil;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GenerationRepository generationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    @Test
    @DisplayName("findById() Test")
    public void findById() throws Exception {
        // given
        // 정보를 보고 싶은 멤버
        Member member = Member.builder()
                .googleEmail("googleEmail")
                .name("name")
                .profileImagePath("profileImagePath")
                .build();
        memberRepository.save(member);

        // 정보를 보려는 멤버
        Member member2 = Member.builder()
                .googleEmail("googleEmail")
                .name("name")
                .profileImagePath("profileImagePath")
                .build();
        memberRepository.save(member2);

        TokenClaimVo vo = new TokenClaimVo(member2.getId(), member2.getEmail());
        String accessToken = jwtUtil.generateToken(true, vo);

        // mockMvc 요청 구성
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/member/{id}", member.getId())
                .header("Authorization", "Bearer " + accessToken);  // AccessToken 추가

        // mockMvc 실행 및 결과 확인
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Transactional
    @Test
    @DisplayName("findGenerationById() Test")
    public void findGenerationById() throws Exception {
        // given
        // 정보를 보고 싶은 멤버
        Member member = Member.builder()
                .googleEmail("googleEmail")
                .name("name")
                .profileImagePath("profileImagePath")
                .build();
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

        // 정보를 보려는 멤버
        Member member2 = Member.builder()
                .googleEmail("googleEmail")
                .name("name")
                .profileImagePath("profileImagePath")
                .build();
        memberRepository.save(member2);

        // 정보를 보려는 멤버의 액세스 토큰 생성
        TokenClaimVo vo = new TokenClaimVo(member2.getId(), member2.getEmail());
        String accessToken = jwtUtil.generateToken(true, vo);

        // mockMvc 요청 구성(헤더에 토큰을 담아 요청을 보냄)
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/member/generation/{id}", member.getId())
                .header("Authorization", "Bearer " + accessToken);  // AccessToken 추가

        // mockMvc 실행 및 결과 확인
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}

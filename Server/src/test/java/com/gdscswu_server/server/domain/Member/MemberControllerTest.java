package com.gdscswu_server.server.domain.Member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.member.dto.ProfileSaveRequestDto;
import com.gdscswu_server.server.global.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class MemberControllerTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @DisplayName("profileSave() 테스트")
    public void profileSave() throws Exception {

        Member member = Member.builder()
                .name("lee")
                .profileImagePath("aaaa")
                .major("major")
                .admissionYear(11111)
                .introduction("hi")
                .email("aaaaa@com")
                .build();

        ProfileSaveRequestDto requestDto = ProfileSaveRequestDto.builder()
                .name("lee")
                .profileImagePath("aaaa")
                .major("major")
                .admissionYear(11111)
                .introduction("hi")
                .email("aaaaa@com")
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        String accessToken= jwtUtil.generateTokens(member).getAccessToken();


        mockMvc.perform(post("/api/v1/member")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                        .andExpect(status().isOk())
                        .andDo(print());

    }
}
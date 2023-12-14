package com.gdscswu_server.server.domain.Member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.member.dto.ProfileSaveRequestDto;
import com.gdscswu_server.server.global.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Slf4j
@Transactional
public class MemberControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;


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
}
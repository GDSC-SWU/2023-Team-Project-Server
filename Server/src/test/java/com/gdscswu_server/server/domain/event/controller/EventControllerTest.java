package com.gdscswu_server.server.domain.event.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscswu_server.server.ServerApplication;
import com.gdscswu_server.server.domain.event.domain.Attendance;
import com.gdscswu_server.server.domain.event.domain.AttendanceRepository;
import com.gdscswu_server.server.domain.event.domain.Event;
import com.gdscswu_server.server.domain.event.domain.EventRepository;
import com.gdscswu_server.server.domain.event.service.EventService;
import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.member.dto.LoginResponseDto;
import com.gdscswu_server.server.domain.model.TokenClaimVo;
import com.gdscswu_server.server.global.util.JwtUtil;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@ContextConfiguration(classes = ServerApplication.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    EventService eventService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("회원 출석 현황 테스트 성공")
    public void get_출석현황() throws Exception {
        // given

        // 멤버 생성
        Member member=Member.builder().googleEmail("temp@gmail").name("swuni").profileImagePath("/path/file1").build();
        memberRepository.save(member);
        Long memberId=member.getId();

        // 일정 생성
        // Event 객체 생성
        Event event = Event.builder().title("meeting 1").date(LocalDate.now()).build();
        eventRepository.save(event);
        Long eventId=event.getId();

        // 참석 여부 생성
        // Attendance 객체 생성
        Attendance attendance= Attendance.builder().event(event).member(member).build();
        attendanceRepository.save(attendance);


        // 토큰
        LoginResponseDto loginResponseDto = jwtUtil.generateTokens(member);


        String url = "http://localhost:" + port + "/api/v1/event/"+memberId;

        // API 호출 및 응답값 출력
        mvc.perform(get(url)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginResponseDto.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk());


    }


}

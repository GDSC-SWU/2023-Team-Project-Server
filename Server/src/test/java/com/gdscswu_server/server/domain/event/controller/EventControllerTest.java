package com.gdscswu_server.server.domain.event.controller;


import com.gdscswu_server.server.ServerApplication;
import com.gdscswu_server.server.domain.event.service.EventService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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
    EventService eventService;

    @Autowired
    private MockMvc mvc;


    @Test
    @DisplayName("회원 출석 현황 테스트 성공")
    public void get_출석현황() throws Exception {


    }


}

package com.gdscswu_server.server.domain.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
//@RequestMapping("/api/v1/member")
public class MemberController {

    @GetMapping("golog")
    public void index() {
      log.info("들어옴");
    }
}

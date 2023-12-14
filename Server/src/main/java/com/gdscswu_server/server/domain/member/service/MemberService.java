package com.gdscswu_server.server.domain.member.service;

import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.member.dto.ProfileSaveRequestDto;
import com.gdscswu_server.server.global.error.GlobalErrorCode;
import com.gdscswu_server.server.global.error.exception.ApiException;
import com.gdscswu_server.server.global.util.ProfileImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProfileImageUtil profileImageUtil;

    @Transactional
    public Long profileSave(Member user, MultipartFile file, ProfileSaveRequestDto requestDto) throws IOException {
        user = memberRepository.findById(user.getId()).orElseThrow(() -> new ApiException(GlobalErrorCode.USER_NOT_FOUND));
        
        // 이미지 처리
        profileImageUtil.uploadProfileImage(file, user);

        return user.update(requestDto).getId();
    }
}
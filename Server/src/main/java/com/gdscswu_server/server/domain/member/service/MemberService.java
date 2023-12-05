package com.gdscswu_server.server.domain.member.service;

import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.member.dto.ProfileSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long profileSave(ProfileSaveRequestDto requestDto){
       return memberRepository.save(requestDto.toEntity()).getId();
    }
}
package com.gdscswu_server.server.domain.member.service;

import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.member.dto.ProfileRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long profileSave(ProfileRequestDto requestDto){
        return memberRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long profileUpdate(Long id, ProfileRequestDto requestDto){
        Member member = memberRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 사용자 없음. id"+ id));
        member.update(requestDto.getName(), requestDto.getProfileImagePath(), requestDto.getMajor(), requestDto.getAdmissionYear()
                , requestDto.getEmail(), requestDto.getIntroduction());
        return id;
    }
}

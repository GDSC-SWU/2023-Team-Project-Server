package com.gdscswu_server.server.domain.member.service;

import com.gdscswu_server.server.domain.member.domain.GenerationRepository;
import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.member.domain.ProjectRepository;
import com.gdscswu_server.server.domain.member.dto.GenerationResponseDto;
import com.gdscswu_server.server.domain.member.dto.MemberResponseDto;
import com.gdscswu_server.server.domain.member.dto.ProjectResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GenerationRepository generationRepository;
    private final ProjectRepository projectRepository;

    // Read Member Information(Member Info, Generation, Project)
    @Transactional(readOnly = true)
    public MemberResponseDto findById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isEmpty()) {
            throw new IllegalArgumentException("해당 멤버가 없습니다. id = " + id);
        }

        List<GenerationResponseDto> generationResponseDtoList =
                generationRepository.findAllByMemberOrderByNumberDesc(optionalMember.get()).stream()
                    .map(GenerationResponseDto::new)
                    .collect(Collectors.toList());
        List<ProjectResponseDto> projectResponseDtoList = projectRepository.findAllByMemberOrderByGenerationDesc(optionalMember.get()).stream()
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());

        return new MemberResponseDto(optionalMember.get(), generationResponseDtoList, projectResponseDtoList);
    }
}

package com.gdscswu_server.server.domain.networking.service;

import com.gdscswu_server.server.domain.member.domain.*;
import com.gdscswu_server.server.domain.networking.domain.Bookmark;
import com.gdscswu_server.server.domain.networking.domain.BookmarkRepository;
import com.gdscswu_server.server.domain.networking.dto.FilterOptionsRequestDto;
import com.gdscswu_server.server.domain.networking.dto.GenerationResponseDto;
import com.gdscswu_server.server.domain.networking.dto.MemberResponseDto;
import com.gdscswu_server.server.domain.networking.dto.ProjectResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NetworkService {
    public final MemberRepository memberRepository;
    public final GenerationRepository generationRepository;
    public final ProjectRepository projectRepository;
    public final BookmarkRepository bookmarkRepository;

    @Transactional
    public List<MemberResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(this::createMemberResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MemberResponseDto> bookmarkMember(Long memberIdToBookmark) {
        Member memberToBookmark = memberRepository.findById(memberIdToBookmark)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberIdToBookmark));

        // Check if the bookmark already exists
        boolean hasBookmark = hasBookmark(memberIdToBookmark);
        if (hasBookmark) {
            bookmarkRepository.deleteByMemberId(memberIdToBookmark);
        } else {
            bookmarkRepository.save(new Bookmark(memberToBookmark, memberToBookmark));
        }

        return findAllMembers();
    }
    @Transactional
    private MemberResponseDto createMemberResponseDto(Member member) {
        boolean bookmark = hasBookmark(member.getId());
        List<GenerationResponseDto> generationResponseDtoList = createGenerationResponseDtoList(member);
        return MemberResponseDto.builder()
                .member(member)
                .bookmark(bookmark)
                .generationResponseDtoList(generationResponseDtoList)
                .build();
    }

    @Transactional
    private boolean hasBookmark(Long memberId) {
        return bookmarkRepository.existsByMemberId(memberId);
    }

    @Transactional
    private List<GenerationResponseDto> createGenerationResponseDtoList(Member member) {
        List<Generation> generations = generationRepository.findByMember(member);
        return generations.stream()
                .map(generation -> {
                    List<ProjectResponseDto> projectResponseDtoList = createProjectResponseDtoList(member, generation);
                    return GenerationResponseDto.builder()
                            .generation(generation)
                            .projectResponseDtoList(projectResponseDtoList)
                            .build();
                })
                .collect(Collectors.toList());
    }
    @Transactional
    private List<ProjectResponseDto> createProjectResponseDtoList(Member member, Generation generation) {
        List<Project> projects = projectRepository.findByMemberAndGeneration(member, generation);

        Set<String> existingParts = new HashSet<>();

        return projects.stream()
                .filter(project -> existingParts.add(project.getPart())) // 중복된 part가 없는 경우만 필터링
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());
    }

}
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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> findAllMembers() {
        try {
            List<Member> members = memberRepository.findAll();

            List<MemberResponseDto> memberResponseDtos = members.stream()
                    .map(this::createMemberResponseDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(memberResponseDtos);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @Transactional
    public ResponseEntity<Object> bookmarkMember(Long memberIdToBookmark) {
        try {
            // 해당 id의 멤버를 Member db 에서 찾아오기
            Member memberToBookmark = memberRepository.findById(memberIdToBookmark)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberIdToBookmark));
            // 해당 멤버가 북마크가 되어있는지 검사해오기 (북마크 되어있음 -> true)
            boolean hasBookmark = bookmarkRepository.existsByTargetMemberId(memberIdToBookmark);
            // 북마크가 되어있다면 삭제
            if (hasBookmark) {
                bookmarkRepository.deleteByTargetMemberId(memberIdToBookmark);
            } else {
                bookmarkRepository.save(new Bookmark(memberToBookmark, memberToBookmark));
            }
            return ResponseEntity.ok(bookmarkRepository.existsByTargetMemberId(memberIdToBookmark));

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @Transactional
    private MemberResponseDto createMemberResponseDto(Member member) {
        // 북마크 db 에서 해당 멤버가 존재하는지 확인
        boolean bookmark = bookmarkRepository.existsByTargetMemberId(member.getId());
        List<GenerationResponseDto> generationResponseDtoList = createGenerationResponseDtoList(member);
        return MemberResponseDto.builder()
                .member(member)
                .bookmark(bookmark)
                .generationResponseDtoList(generationResponseDtoList)
                .build();
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
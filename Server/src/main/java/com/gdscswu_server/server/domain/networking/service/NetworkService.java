package com.gdscswu_server.server.domain.networking.service;

import com.gdscswu_server.server.domain.member.domain.*;
import com.gdscswu_server.server.domain.networking.domain.BookmarkRepository;
import com.gdscswu_server.server.domain.networking.dto.GenerationResponseDto;
import com.gdscswu_server.server.domain.networking.dto.MemberResponseDto;
import com.gdscswu_server.server.domain.networking.dto.ProjectResponseDto;
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


    public List<MemberResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(this::createMemberResponseDto)
                .collect(Collectors.toList());
    }

    private MemberResponseDto createMemberResponseDto(Member member) {
        boolean bookmark = false; // You may set this based on your logic
        List<GenerationResponseDto> generationResponseDtoList = createGenerationResponseDtoList(member);
        return MemberResponseDto.builder()
                .member(member)
                .bookmark(bookmark)
                .generationResponseDtoList(generationResponseDtoList)
                .build();
    }

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

    private List<ProjectResponseDto> createProjectResponseDtoList(Member member, Generation generation) {
        List<Project> projects = projectRepository.findByMemberAndGeneration(member, generation);

        Set<String> existingParts = new HashSet<>();

        return projects.stream()
                .filter(project -> existingParts.add(project.getPart())) // 중복된 part가 없는 경우만 필터링
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());
    }


    /*@Transactional(readOnly = true)
    public List<MemberListResponseDto> findFilteredMembers(String department, String part, String level) {
        // 필요한 로직을 구현해 필터링된 멤버를 가져오세요.
        List<Member> filteredMembers = // ...

        return mapToMemberListResponseDtoList(filteredMembers, false); // 북마크 값은 여기서 고정으로 설정
    }*/


/*    @Transactional
    public ResponseEntity<Object> addBookmark(){

    }

    @Transactional
    public ResponseEntity<Object> removeBookmark(){

    }*/


}
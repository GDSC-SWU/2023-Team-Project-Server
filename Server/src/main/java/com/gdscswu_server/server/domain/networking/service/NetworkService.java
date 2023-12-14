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
    public ResponseEntity<Object> findAllMembers(FilterOptionsRequestDto filterOptionsRequestDto) {
        try {
            List<Member> members = memberRepository.findAll();

            List<MemberResponseDto> memberResponseDtoList = members.stream()

                    .map(member -> {
                        MemberResponseDto memberResponseDto = createMemberResponseDto(member, filterOptionsRequestDto);
                        return memberResponseDto;
                    })
                    .collect(Collectors.toList());
            memberResponseDtoList = memberResponseDtoList.stream().filter(filteredMembers -> isMemberMatch(filteredMembers, filterOptionsRequestDto)).collect(Collectors.toList());
            return ResponseEntity.ok(memberResponseDtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    private boolean isMemberMatch(MemberResponseDto filteredMembers, FilterOptionsRequestDto filterOptionsRequestDto) {
        // 셋다 true 여야 return 해주는 로직
        boolean partMatch;
        boolean levelMatch;
        boolean departmentMatch;

        // containsAll 메서드는 파라미터로 전달된 컬렉션이 비어있으면 무조건 true 를 반환
        // 조건이 없으면 다 true 처리 함 (어차피 다른 조건 안맞으면 못 나감)
        partMatch =
                filteredMembers.getGenerationList().stream()
                        .flatMap(generationResponseDto -> generationResponseDto.getProjectList().stream())
                        .map(ProjectResponseDto::getPart)
                        .collect(Collectors.toList())
                        // filteredMembers 의 part 조건이 filterOptionsRequestDto 의 모든 조건을 포함하고 있는지 확인
                        // 필터링을 하고자 하는 조건을 멤버가 전부 가져야 함
                        .containsAll(filterOptionsRequestDto.getParts());
        levelMatch =
                filteredMembers.getGenerationList().stream()
                        .map(GenerationResponseDto::getLevel)
                        .collect(Collectors.toList())
                        .containsAll(filterOptionsRequestDto.getLevels());
        departmentMatch =
                filteredMembers.getGenerationList().stream()
                        .map(GenerationResponseDto::getDepartment)
                        .collect(Collectors.toList())
                        .containsAll(filterOptionsRequestDto.getDepartments());


        /*if(filterOptionsRequestDto.getParts().isEmpty()){
            // 파트 필터링 조건 없으면 true
            partMatch= true;
        }else {
            partMatch =
                    filteredMembers.getGenerationResponseDtoList().stream()
                            .flatMap(generationResponseDto -> generationResponseDto.getProjectResponseDtoList().stream())
                            .map(ProjectResponseDto::getPart)
                            .collect(Collectors.toList())
                            .containsAll(filterOptionsRequestDto.getParts());
        }
        if(filterOptionsRequestDto.getLevels().isEmpty()){
            // 레벨 필터링 조건 없으면 true
            levelMatch=true;
        }else{
            levelMatch =
                    filteredMembers.getGenerationResponseDtoList().stream()
                            .map(GenerationResponseDto::getLevel)
                            .collect(Collectors.toList())
                            .containsAll(filterOptionsRequestDto.getLevels());
        }
        if(filterOptionsRequestDto.getDepartments().isEmpty()){
            // 부서 조건 없으면 true 리턴
            departmentMatch=true;
        }else{
            departmentMatch =
                    filteredMembers.getGenerationResponseDtoList().stream()
                            .map(GenerationResponseDto::getDepartment)
                            .collect(Collectors.toList())
                            .containsAll(filterOptionsRequestDto.getDepartments());
        }
*/
        //int count = filterOptionsRequestDto.getS


        return partMatch && levelMatch && departmentMatch;

    }


    @Transactional
    private MemberResponseDto createMemberResponseDto(Member member, FilterOptionsRequestDto filterOptionsRequestDto) {
        // 북마크 db 에서 해당 멤버가 존재하는지 확인, bookmark 값 정하기
        boolean bookmark = bookmarkRepository.existsByTargetMemberId(member.getId());
        // generationResponseDtoList 만들기 위해 함수 호출
        List<GenerationResponseDto> generationResponseDtoList = createGenerationResponseDtoList(member, filterOptionsRequestDto);
        // MemberResponseDto 생성
        return MemberResponseDto.builder()
                .member(member)
                .bookmark(bookmark)
                .generationList(generationResponseDtoList)
                .build();
    }

    @Transactional
    private List<GenerationResponseDto> createGenerationResponseDtoList(Member member, FilterOptionsRequestDto filterOptionsRequestDto) {
        // 해당 멤버 Generation
        List<Generation> generationList = generationRepository.findByMember(member);
        return generationList.stream()
                .map(generation -> {
                    // projectResponseDtoList 만들기 위해서 함수 호출
                    List<ProjectResponseDto> projectResponseDtoList = createProjectResponseDtoList(member, generation, filterOptionsRequestDto);
                    // GenerationResponseDto 생성
                    return GenerationResponseDto.builder()
                            .generation(generation)
                            .projectList(projectResponseDtoList)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    private List<ProjectResponseDto> createProjectResponseDtoList(Member member, Generation generation, FilterOptionsRequestDto filterOptionsRequestDto) {
        List<Project> projects = projectRepository.findByMemberAndGeneration(member, generation);

        Set<String> existingParts = new HashSet<>();

        return projects.stream()
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());
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

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
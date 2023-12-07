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

                List<MemberResponseDto> memberResponseDtos = members.stream()
                        .map(member -> createMemberResponseDto(member,filterOptionsRequestDto))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(memberResponseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @Transactional
    private MemberResponseDto createMemberResponseDto(Member member, FilterOptionsRequestDto filterOptionsRequestDto) {
        // 북마크 db 에서 해당 멤버가 존재하는지 확인, bookmark 값 정하기
        boolean bookmark = bookmarkRepository.existsByTargetMemberId(member.getId());
        // generationResponseDtoList 만들기 위해 함수 호출
        List<GenerationResponseDto> generationResponseDtoList = createGenerationResponseDtoList(member, filterOptionsRequestDto);
        // 필터링
        generationResponseDtoList=generationResponseDtoList.stream()
                .filter(generationList -> (filterOptionsRequestDto.getDepartments().isEmpty() || filterOptionsRequestDto.getDepartments().contains(generationList.getDepartment())) ||
                        (filterOptionsRequestDto.getLevels().isEmpty() || filterOptionsRequestDto.getLevels().contains(generationList.getLevel())))
                .collect(Collectors.toList());

        // MemberResponseDto 생성
        return MemberResponseDto.builder()
                .member(member)
                .bookmark(bookmark)
                .generationResponseDtoList(generationResponseDtoList)
                .build();
    }

    @Transactional
    private List<GenerationResponseDto> createGenerationResponseDtoList(Member member, FilterOptionsRequestDto filterOptionsRequestDto) {
        // 해당 멤버 Generation
        List<Generation> generationList = generationRepository.findByMember(member);
        return generationList.stream()
                /*.filter(generation ->
                        (filterOptionsRequestDto.getDepartments().isEmpty() || filterOptionsRequestDto.getDepartments().contains(generation.getDepartment())) ||
                                (filterOptionsRequestDto.getLevels().isEmpty() || filterOptionsRequestDto.getLevels().contains(generation.getLevel()))) */// Filter based on department and level
                .map(generation -> {
                    // projectResponseDtoList 만들기 위해서 함수 호출
                    List<ProjectResponseDto> projectResponseDtoList = createProjectResponseDtoList(member, generation, filterOptionsRequestDto);
                    // 필터링
                    projectResponseDtoList=projectResponseDtoList.stream()
                            .filter(projectList -> (filterOptionsRequestDto.getParts().isEmpty() || filterOptionsRequestDto.getParts().contains(projectList.getPart())))
                            .collect(Collectors.toList());

                    // GenerationResponseDto 생성
                    return GenerationResponseDto.builder()
                            .generation(generation)
                            .projectResponseDtoList(projectResponseDtoList)
                            .build();
                })
                .collect(Collectors.toList());
    }
    @Transactional
    private List<ProjectResponseDto> createProjectResponseDtoList(Member member, Generation generation, FilterOptionsRequestDto filterOptionsRequestDto) {
        List<Project> projects = projectRepository.findByMemberAndGeneration(member, generation);

        Set<String> existingParts = new HashSet<>();

        return projects.stream()
                // 중복된 part가 있으면 그 project 는 제외
                // 필터링 조건 있다면 조건 부합하지 않는 project 제외
                .filter(project -> existingParts.add(project.getPart()))
                        /*(filterOptionsRequestDto.getParts().isEmpty() || filterOptionsRequestDto.getParts().contains(project.getPart())))*/ // Filter based on parts
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

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    /*
    private boolean isMemberMatch(Member member, FilterOptionsRequestDto filterOptionsRequestDto) {
        // 필터링 하려고 들어온 부서, 파트, 레벨 값이 전부 비어있으면 (필터링 조건이 없으면)
        // 모든 멤버를 포함해줘야 해서 항상 true 리턴
        if (filterOptionsRequestDto.getDepartments().isEmpty() &&
                filterOptionsRequestDto.getParts().isEmpty() &&
                filterOptionsRequestDto.getLevels().isEmpty()){
            return true;
        }
        // 필터링 조건에 부합하는 사람은 true return 하여 포함 시키기
        // 필터링 조건에 부합하지 않는 사람은 false return 하여 제외 시키기
    }
*/

/*    @Transactional
    private List<MemberResponseDto> filteringMembers(String filterCondition){
        List<Member> members = memberRepository.findAll();
        // 조건에 의해 필터링 된 일부 멤버 리스트가 담긴 List<MemberResponseDto>
        List<MemberResponseDto> memberResponseDtos;

        // 프론트에 조건에 의해 필터링 된 일부 멤버 리스트가 담긴 List<MemberResponseDto> 전달
        return memberResponseDtos;

    }*/

}
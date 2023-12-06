package com.gdscswu_server.server.domain.networking.service;

import com.gdscswu_server.server.domain.member.domain.*;
import com.gdscswu_server.server.domain.networking.domain.BookmarkRepository;
import com.gdscswu_server.server.domain.networking.dto.MemberDetailResponseDto;
import com.gdscswu_server.server.domain.networking.dto.MemberListResponseDto;
import com.gdscswu_server.server.domain.networking.dto.MemberListUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NetworkService {
    public final MemberRepository memberRepository;
    public final GenerationRepository generationRepository;
    public final ProjectRepository projectRepository;
    public final BookmarkRepository bookmarkRepository;

    public List<MemberListResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return mapToMemberListResponseDtoList(members, false);

    }

    private  List<MemberListResponseDto> mapToMemberListResponseDtoList(List<Member> members, boolean bookmark) {
        return members.stream()
                .map(member -> {
                    List<MemberDetailResponseDto> memberDetailResponseDtoList = getMemberDetails(member);
                    return new MemberListResponseDto(member, bookmark, memberDetailResponseDtoList);
                })
                .collect(Collectors.toList());
    }

    private List<MemberDetailResponseDto> getMemberDetails(Member member) {
        List<Generation> generations = generationRepository.findByMember(member);
        List<Project> projects = projectRepository.findByMember(member);

        return generations.stream()
                .map(generation -> new MemberDetailResponseDto(generation, projects))
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

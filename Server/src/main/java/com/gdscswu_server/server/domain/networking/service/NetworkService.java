package com.gdscswu_server.server.domain.networking.service;

import com.gdscswu_server.server.domain.member.domain.Generation;
import com.gdscswu_server.server.domain.member.domain.GenerationRepository;
import com.gdscswu_server.server.domain.networking.dto.MemberListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NetworkService {
    public final MemberRepository memberRepository;
    public final GenerationRepository generationRepository;

    // 멤버 보여주기
    @Transactional (readOnly = true)
    public ResponseEntity<Object> findAllMembers() {
        try {
            // member 찾아서 리스트로 수집하여 반환
            List<MemberListResponseDto> memberList = generationRepository.findAll().stream()
                    .map(MemberListResponseDto::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(memberList);
        } catch (Exception e) {
            // 예외가 발생한 경우 클라이언트에게 적절한 응답을 보내줌
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("멤버 리스트 조회 실패" + e.getMessage());

        }
    }


    // 유저(나) 보여주기
//    @Transactional (readOnly = true)
//    public ResponseEntity<Object> findByUser (Long userId){
//        try{
//            // 로그인 한 유저 (나) 생성될 때 담았던 id 값으로 디비에서 "나" 찾아오기
//            Generation user = generationRepository.findById(userId)
//                    .orElseThrow(() -> new IllegalArgumentException());
//
//            UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto(user);
//            return ResponseEntity.ok("유저 조회 성공 "+ userProfileResponseDto);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("유저 조회 실패: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("서버 오류: " + e.getMessage());
//        }
//    }
}

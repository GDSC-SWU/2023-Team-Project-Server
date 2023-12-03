//package com.gdscswu_server.server.domain.networking.dto;
//
//import com.gdscswu_server.server.domain.member.domain.Generation;
//import com.gdscswu_server.server.domain.member.domain.Member;
//import com.gdscswu_server.server.domain.member.domain.Project;
//
//
//public class UserProfileResponseDto {
//    private Long id;
//    private String name; // 이름
//    private String profileImagePath; // 프로필 이미지
//    private Integer number; // 기수 (1기, 2기...)
//    private String department; // 부서
//    private String level; // 레벨 (코어, 리드...)
//    private String part; // 파트 (피엠, 디자인...)
//
//    // 생성자
//    public UserProfileResponseDto(Generation generation){
//        this.id=generation.getId();
//        this.name=generation.getMember().getName();
//        this.profileImagePath=generation.getMember().getProfileImagePath();
//        this.number=generation.getNumber();
//        this.department=generation.getDepartment();
//        this.level=generation.getLevel();
//        //this.part=project.getPart(); // 파트 처리 고민중...
//    }
//}

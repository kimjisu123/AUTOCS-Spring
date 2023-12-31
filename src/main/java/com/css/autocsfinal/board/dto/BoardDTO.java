package com.css.autocsfinal.board.dto;

import com.css.autocsfinal.member.dto.MemberDTO;
import lombok.*;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardDTO {

    private int boardNo;

    private String title;

    private String content;

    private Date regist;

    private Date modify;

    private Date delete;

    private char anonymity;

    private int refCategoryNo;

    private int refMemberNo;

    private List<String> fileUrls;

    //게시판에서 직원 직급과 부서와 이름 담을거
    private String employeeName;
    private String department;
    private String position;
    private String memnerRole;

    //게시판에서 영업점
    private String storeName;

    //분리
    private BoardCategoryDTO category;
    private MemberDTO member;
}

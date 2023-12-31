package com.css.autocsfinal.board.controller;

import com.css.autocsfinal.board.dto.BoardCommentDTO;
import com.css.autocsfinal.board.dto.BoardDTO;
import com.css.autocsfinal.board.service.CommentService;
import com.css.autocsfinal.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment", description = "게시판 댓글 API")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 등록
    @Operation(summary = "댓글 등록 요청", description = "댓글을 등록합니다.", tags = {"CommentController"})
    @PostMapping(value = "/commentInsert")
    public ResponseEntity<ResponseDTO> writingGo(@ModelAttribute BoardCommentDTO boardCommentDTO) {

        log.info("[BoardCommentDTO] boardCommentDTO {} =======> " + boardCommentDTO);

        String resultMessage = commentService.insertBoardComment(boardCommentDTO);

            HttpStatus httpStatus = (resultMessage.contains("성공")) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

            return ResponseEntity
                    .status(httpStatus)
                    .body(new ResponseDTO(httpStatus, resultMessage, null));
    }

    //댓글 전체 조회
    @Operation(summary = "모든 댓글 조회 요청", description = "모든 댓글을 조회합니다.", tags = {"CommentController"})
    @GetMapping("/getBoardCommentAll")
    public ResponseEntity<ResponseDTO> getBoardCommentAll() {

        System.out.println("check ==========================");
        List<BoardCommentDTO> commentDTO = commentService.getAllComment();

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDTO responseDTO = new ResponseDTO(httpStatus, "모든 댓글 조회 성공", commentDTO);

        return ResponseEntity.status(httpStatus).body(responseDTO);

    }

    //댓글 수정
    @Operation(summary = "댓글 수정 요청", description = "댓글을 수정합니다.", tags = {"CommentController"})
    @PostMapping("/updateComment")
    public ResponseEntity<ResponseDTO> updateComment(@ModelAttribute BoardCommentDTO boardCommentDTO) {
        log.info("[BoardCommentDTO] boardCommentDTO {} =======> " + boardCommentDTO);

        String resultMessage = commentService.updateComment(boardCommentDTO);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDTO responseDTO = new ResponseDTO(httpStatus, "댓글 수정 성공", resultMessage);

        return ResponseEntity.status(httpStatus).body(responseDTO);

    }

    //댓글 삭제
    @Operation(summary = "댓글 삭제 요청", description = "댓글을 삭제합니다.", tags = {"CommentController"})
    @PostMapping("/deleteComment")
    public ResponseEntity<ResponseDTO> deleteComment(@RequestParam int commentNo) {
        log.info("[BoardCommentDTO] commentNo {} =======> " + commentNo);

        String comment = commentService.deleteComment(commentNo);

        HttpStatus httpStatus = HttpStatus.OK;

        ResponseDTO responseDTO = new ResponseDTO(httpStatus, "댓글 삭제 성공", comment);

        return ResponseEntity.status(httpStatus).body(responseDTO);

    }
}

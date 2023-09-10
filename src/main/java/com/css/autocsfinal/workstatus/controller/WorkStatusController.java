package com.css.autocsfinal.workstatus.controller;

import com.css.autocsfinal.common.*;
import com.css.autocsfinal.workstatus.dto.WorkStatusDTO;
import com.css.autocsfinal.workstatus.repository.WorkStatusListRepository;
import com.css.autocsfinal.workstatus.service.WorkStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class WorkStatusController {

    private final WorkStatusService workStatusService;

    @GetMapping("/workStatus/{employeeNo}")
    public ResponseEntity<ResponseDTO> findByAll(@PathVariable int employeeNo){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "근태 관리 조회 성공",  workStatusService.findByEmployeeNo(employeeNo)));
    }

    @GetMapping("/department")
    public ResponseEntity<ResponseDTO> findByDepartmentAll(){

        log.info("============================?>>");
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "본사 근태 관리 조회 성공", workStatusService.findByDepartmentAll()));
    }

    // 인사부 조회
    @GetMapping("/personnel")
    public ResponseEntity<ResponseDTO> findByPersonnel(){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "인사부 조회 성공", workStatusService.findByPersonnel()));
    }

    // 재무/회계부 조회
    @GetMapping("/accounting")
    public ResponseEntity<ResponseDTO> findByAccounting(){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "재무/회계부 조회 성공", workStatusService.findByAccounting()));
    }

    // 경영부 조회
    @GetMapping("/management")
    public ResponseEntity<ResponseDTO> findByManagement(){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "경영부 조회 성공", workStatusService.findByManagement()));
    }

    // 마케팅부 조회
    @GetMapping("/marketing")
    public ResponseEntity<ResponseDTO> findByMarketing(){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "마케팅부 조회 성공", workStatusService.findByMarketing()));
    }

    // 영업부 조회
    @GetMapping("/sales")
    public ResponseEntity<ResponseDTO> findBySales(){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "영업부 조회 성공", workStatusService.findBySales()));
    }

    // 서비스부 조회
    @GetMapping("/service")
    public ResponseEntity<ResponseDTO> findByService(){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "서비스부 조회 성공", workStatusService.findByService()));
    }

    // 출근하기
    @PostMapping("/attendance/{employeeNo}")
    public ResponseEntity<ResponseDTO> saveAttendance(@PathVariable int employeeNo ){


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "출근 성공", workStatusService.saveAttendance(employeeNo)));
    }

    // 퇴근
    @PutMapping("/quitting/{employeeNo}")
    public ResponseEntity<ResponseDTO> saveQuitting(@PathVariable int employeeNo){

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "퇴근 성공", workStatusService.saveQuitting()));
    }

    // 본사 근태 현황

    @GetMapping("/headOffice/{page}")
    public ResponseEntity<ResponseDTO> findByHeadOffice( @PathVariable(name = "page", required = false) int offset){


        int total = workStatusService.findByHeadOfficeTotal();

        Criteria cri = new Criteria(Integer.valueOf(offset), 8);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(workStatusService.findByHeadOffice(cri));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "본사 근태관리 조회 성공", pagingResponseDTO));
    }
}

























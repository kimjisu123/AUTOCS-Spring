package com.css.autocsfinal.stock.controller;

import com.css.autocsfinal.common.Criteria;
import com.css.autocsfinal.common.PageDTO;
import com.css.autocsfinal.common.PagingResponseDTO;
import com.css.autocsfinal.common.ResponseDTO;
import com.css.autocsfinal.stock.dto.IoDTO;
import com.css.autocsfinal.stock.dto.IoSummaryDTO;
import com.css.autocsfinal.stock.dto.StatisticsDTO;
import com.css.autocsfinal.stock.service.IoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@Slf4j
@Tag(name = " Io", description = "물품 입출고 API")
public class IoController {

    private final IoService ioService;

    public IoController(IoService ioService) {
        this.ioService = ioService;
    }

    /* 입출고 입력 */
    @Operation(summary = "입출고 입력 요청", description = "입출고 입력 진행됩니다.", tags = { "IoController" })
    @PostMapping("/stock/stockio")
    public ResponseEntity<ResponseDTO> insertIo(@ModelAttribute IoDTO ioDTO){

        return ResponseEntity.ok()
                .body(new ResponseDTO(HttpStatus.OK, "입출고 입력 성공"
                        , ioService.insertIo(ioDTO)));
    }

    /* 입출고 조회 */
    @Operation(summary = "입출고 전체조회 요청", description = "입출고 전체조회 진행됩니다.", tags = { "IoController" })
    @GetMapping("/stock/stockio")
    public ResponseEntity<ResponseDTO> selectIoListWithPaging(
            @RequestParam(name = "offset", defaultValue = "1") String offset){

        int total = ioService.selectIoAll();

        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(ioService.selectIoListWithPaging(cri));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", pagingResponseDTO));
    }


    /* 입출고 조회 그룹화 - 이름검색, 페이징 필요 날짜 인자 받기*/
    @Operation(summary = "입출고 물품이름별 조회 요청", description = "입출고 물품이름별 조회 진행됩니다.", tags = { "IoController" })
    @GetMapping("/stock/check")
    public ResponseEntity<ResponseDTO> summarizeWithPaging(
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @RequestParam(name = "s", defaultValue = "")String s,
            @RequestParam(name = "startDate", defaultValue = "")Date startDate,
            @RequestParam(name = "endDate", defaultValue = "")Date endDate){

        log.info("check1");
        log.info("offset  ==============> {} ", offset);
        log.info("s================={}", s);
        log.info("startDate =================={},", startDate);
        log.info("endDate ================={}", endDate);

        int total = ioService.summarizeSize(s, startDate, endDate);
        log.info("check1=============> {}", total);

        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        List<Tuple> tuplePage = ioService.summarize(cri, s, startDate, endDate);

        List<IoSummaryDTO> ioSummaryPage = tuplePage.stream()
                .map(tuple -> {
                    BigDecimal productNo =  tuple.get(0, BigDecimal.class);
                    String categoryName = tuple.get(1, String.class);
                    String productName = tuple.get(2, String.class);
                    String standardName = tuple.get(3, String.class);
                    String unitName = tuple.get(4, String.class);
                    BigDecimal stock = tuple.get(5, BigDecimal.class);
                    BigDecimal price = tuple.get(6, BigDecimal.class);
                    String etc = tuple.get(7, String.class);
                    BigDecimal currentQuantity = tuple.get(8, BigDecimal.class);
                    BigDecimal totalQuantityIn = tuple.get(9, BigDecimal.class);
                    BigDecimal completeQuantity = tuple.get(10, BigDecimal.class);
                    BigDecimal refundQuantity = tuple.get(11, BigDecimal.class);
                    BigDecimal totalQuantityOut = tuple.get(12, BigDecimal.class);

                    return new IoSummaryDTO(productNo.intValue(), categoryName, productName, standardName, unitName,
                            stock.intValue(), price.intValue(), etc, currentQuantity.intValue(),
                            totalQuantityIn.intValue(), completeQuantity.intValue(), refundQuantity.intValue(),
                            totalQuantityOut.intValue());
                })
                .collect(Collectors.toList());


        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(ioSummaryPage);
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", pagingResponseDTO));
    }

    /* 매출통계 - 이름검색, 페이징 필요 날짜 인자 받기*/
    @Operation(summary = "매출통계 물품이름별 조회 요청", description = "매출통계 물품이름별 조회 진행됩니다.", tags = { "IoController" })
    @GetMapping("/stock/statistics")
    public ResponseEntity<ResponseDTO> statistics(
            @RequestParam(name = "s", defaultValue = "")String s,
            @RequestParam(name = "startDate", defaultValue = "")Date startDate,
            @RequestParam(name = "endDate", defaultValue = "")Date endDate){

        List<Tuple> tuplePage = ioService.statistics(s, startDate, endDate);

        List<IoSummaryDTO> ioSummaryPage = tuplePage.stream()
                .map(tuple -> {
                    BigDecimal productNo =  tuple.get(0, BigDecimal.class);
                    String categoryName = tuple.get(1, String.class);
                    String productName = tuple.get(2, String.class);
                    String standardName = tuple.get(3, String.class);
                    String unitName = tuple.get(4, String.class);
                    BigDecimal stock = tuple.get(5, BigDecimal.class);
                    BigDecimal price = tuple.get(6, BigDecimal.class);
                    String etc = tuple.get(7, String.class);
                    BigDecimal currentQuantity = tuple.get(8, BigDecimal.class);
                    BigDecimal totalQuantityIn = tuple.get(9, BigDecimal.class);
                    BigDecimal completeQuantity = tuple.get(10, BigDecimal.class);
                    BigDecimal refundQuantity = tuple.get(11, BigDecimal.class);
                    BigDecimal totalQuantityOut = tuple.get(12, BigDecimal.class);

                    return new IoSummaryDTO(productNo.intValue(), categoryName, productName, standardName, unitName,
                            stock.intValue(), price.intValue(), etc, currentQuantity.intValue(),
                            totalQuantityIn.intValue(), completeQuantity.intValue(), refundQuantity.intValue(),
                            totalQuantityOut.intValue());
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", ioSummaryPage));
    }


    /* 영업점별 매출통계용 - 이름 날짜 검색*/
    @Operation(summary = "매출통계 영업점,물품이름별 조회 요청", description = "매출통계 영업점,물품이름별 조회 진행됩니다.", tags = { "IoController" })
    @GetMapping("/stock/mystatistics")
    public ResponseEntity<ResponseDTO> statistics(
            @RequestParam(name = "store", defaultValue = "")String store,
            @RequestParam(name = "s", defaultValue = "")String s,
            @RequestParam(name = "startDate", defaultValue = "")Date startDate,
            @RequestParam(name = "endDate", defaultValue = "")Date endDate){

        List<Tuple> tuplePage = ioService.myStatistics(store, s, startDate, endDate);

        List<StatisticsDTO> ioSummaryPage = tuplePage.stream()
                .map(tuple -> {
                    BigDecimal productNo =  tuple.get(0, BigDecimal.class);
                    String categoryName = tuple.get(1, String.class);
                    String productName = tuple.get(2, String.class);
                    String standardName = tuple.get(3, String.class);
                    String unitName = tuple.get(4, String.class);
                    BigDecimal price = tuple.get(5, BigDecimal.class);
                    BigDecimal completeQuantity = tuple.get(6, BigDecimal.class);
                    BigDecimal refundQuantity = tuple.get(7, BigDecimal.class);

                    return new StatisticsDTO(productNo.intValue(), categoryName, productName, standardName, unitName,
                            price.intValue(), completeQuantity.intValue(), refundQuantity.intValue());
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", ioSummaryPage));
    }
//    @GetMapping("/stock/check")
//    public ResponseEntity<ResponseDTO> summarizeWithPaging(
//            @RequestParam(name = "offset", defaultValue = "1") String offset,
//            @RequestParam(name = "s", defaultValue = "")String s,
//            @RequestParam(name = "startDate", defaultValue = "")Date startDate,
//            @RequestParam(name = "endDate", defaultValue = "")Date endDate){
//        log.info("check1");
//        log.info("offset  ==============> {} ", offset);
//        log.info("s================={}", s);
//        log.info("startDate =================={},", startDate);
//        log.info("endDate ================={}", endDate);
//
//        int total = ioService.summarizeSize(s, startDate, endDate);
//        log.info("check1=============> {}", total);
//
//        Criteria cri = new Criteria(Integer.valueOf(offset), 10);
//
//
//        List<Tuple> tuplePage = ioService.summarize(cri,s, startDate, endDate);
//
//        List<IoSummaryDTO> ioSummaryPage = tuplePage.stream()
//                .map(tuple -> {
//                    BigDecimal refProductNo =  tuple.get(0, BigDecimal.class);
//                    BigDecimal totalQuantityIn = tuple.get(1, BigDecimal.class);
//                    BigDecimal totalQuantityOut = tuple.get(2, BigDecimal.class);
////                    int refProductNo = tuple.get(0, Integer.class);
////                    long totalQuantityIn = tuple.get(1, Long.class);
////                    long totalQuantityOut = tuple.get(2, Long.class);
//                    String productName = tuple.get(3, String.class);
//                    String categoryName = tuple.get(4, String.class);
//                    String standardName = tuple.get(5, String.class);
//                    String unitName = tuple.get(6, String.class);
//                    BigDecimal stock = tuple.get(7, BigDecimal.class);
//                    BigDecimal price = tuple.get(8, BigDecimal.class);
////                    int stock = tuple.get(7, Integer.class);
////                    int price = tuple.get(8, Integer.class);
//                    String etc = tuple.get(9, String.class);
//
//                    return new IoSummaryDTO(refProductNo.intValue(), totalQuantityIn.intValue(), totalQuantityOut.intValue(), productName, categoryName, standardName,
//                            unitName, stock.intValue(), price.intValue(), etc );
////                    return new IoSummaryDTO(refProductNo, totalQuantityIn, totalQuantityOut, productName, categoryName, standardName,
////                            unitName, stock, price, etc );
//                })
//                .collect(Collectors.toList());
//
//
//        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
//        pagingResponseDTO.setData(ioSummaryPage);
//        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));
//
//
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", pagingResponseDTO));
//    }




}

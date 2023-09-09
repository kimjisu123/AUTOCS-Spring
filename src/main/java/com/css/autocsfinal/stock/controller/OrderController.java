package com.css.autocsfinal.stock.controller;

import com.css.autocsfinal.common.Criteria;
import com.css.autocsfinal.common.PageDTO;
import com.css.autocsfinal.common.PagingResponseDTO;
import com.css.autocsfinal.common.ResponseDTO;
import com.css.autocsfinal.stock.dto.OrderDTO;
import com.css.autocsfinal.stock.service.OrderService;
import com.css.autocsfinal.stock.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /* 주문번호 전체조회 (불용일등록)*/
    @GetMapping("/stock/order")
    public ResponseEntity<ResponseDTO> selectProductListWithPaging(
            @RequestParam(name = "offset", defaultValue = "1") String offset){

        int total = orderService.selectOrderAll();

        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(orderService.selectOrderListWithPaging(cri));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", pagingResponseDTO));
    }

    /* 마지막 주문번호 조회 */
    @GetMapping("/stock/order/findLast")
    public ResponseEntity<ResponseDTO> selectLastOrder(){
        System.out.println("====================");
        System.out.println(orderService.selectLastOrder());

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공",  orderService.selectLastOrder()));
    }


    /* 주문번호 입력 */
    @PostMapping("/stock/order")
    public ResponseEntity<ResponseDTO> insertOrder(@ModelAttribute OrderDTO orderDTO){

        return ResponseEntity.ok()
                .body(new ResponseDTO(HttpStatus.OK, "주문번호 입력 성공"
                        , orderService.insertOrder(orderDTO)));
    }

    /* 주문번호 입력 */
//    @DeleteMapping("/stock/order/delete")
//    public ResponseEntity<ResponseDTO> deleteOrder(@ModelAttribute int orderNo){
//
//        return ResponseEntity.ok()
//                .body(new ResponseDTO(HttpStatus.OK, "주문번호 삭제 성공"
//                        , orderService.deleteOrder(orderNo)));
//    }

}
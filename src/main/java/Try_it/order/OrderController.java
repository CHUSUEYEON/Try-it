package Try_it.order;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import Try_it.goods.GoodsEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/orders")
@Tag(name = "order", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;
    private final HttpSession httpSession;

    public OrderController(OrderService orderService, HttpSession httpSession) {
        this.orderService = orderService;
        this.httpSession = httpSession;
    }
//
//    @Operation(summary = "주문 추가 API")
//    @PostMapping("/goods/{goodPk}")
//    public ResponseEntity<ResDTO> createOrder(@AuthenticationPrincipal String userPk,
//                                              @RequestParam(required = false) Long couponPk,
//                                              @RequestBody OrderDTO orderDTO,
//                                              @PathVariable Long goodPk){
//        OrderEntity newOrder = orderService.createOrder(userPk, orderDTO, goodPk, couponPk);
//        OrderDTO responseOrder = OrderDTO.builder()
//            .orderPk(newOrder.getOrderPk())
//            .orderCreatedAt(newOrder.getOrderCreatedAt())
//            .orderRecipient(newOrder.getOrderRecipient())
//            .orderTotal(newOrder.getOrderTotal())
//            .orderPhone(newOrder.getOrderPhone())
//            .orderAddress(newOrder.getOrderAddress())
//            .orderRequest(newOrder.getOrderRequest())
//            .orderIsCancelled(newOrder.getOrderIsCancelled())
//            .user(newOrder.getUser().getUserPk())
//            .coupon(couponPk != null ? newOrder.getCoupon().getCouponPk() : null)
//            .build();
//        return ResponseEntity.ok().body(ResDTO.builder()
//           .statusCode(StatusCode.OK)
//           .data(responseOrder)
//           .message("주문리스트에 주문 추가 성공")
//           .build());
//    }

    @Operation(summary = "장바구니에 담긴 전체 상품 주문 임시 저장 API", description = """
            인증/인가 : 로그인 필요\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
    @PostMapping("/goods/temp")
    public ResponseEntity<ResDTO> createCartsTempOrder(@AuthenticationPrincipal String userPk){

        List<OrderDTO> tempOrder =orderService.createCartsTempOrder(userPk);

        httpSession.setAttribute("tempOrder", tempOrder);

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(tempOrder)
            .message("주문 임시 저장 성공")
            .build());
    }

    @Operation(summary = "장바구니에 담긴 전체 상품 주문 API", description = """
            테스트 방법: 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            인증/인가 : 로그인 필요\s
            couponPk : 3 (선택)\s
            \s
            { "orderRequest": "경비실에 맡겨주세요.", "orderRecipient": "추수연", "orderAddress": "서울시 서대문구 홍은동", "orderPhone": "1012345678" }\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
    @PostMapping("/goods")
    public ResponseEntity<ResDTO> createCartsOrder(@AuthenticationPrincipal String userPk,
                                                   @RequestParam(required = false) Long couponPk,
                                                @RequestBody OrderDTO orderDTO
                                                ){
        List<OrderDTO> temporaryOrder = (List<OrderDTO>) httpSession.getAttribute("tempOrder");
        if(temporaryOrder == null){
            return ResponseEntity.badRequest().body(ResDTO.builder()
               .statusCode(StatusCode.BAD_REQUEST)
               .message("임시 주문 정보를 찾을 수 없습니다.")
               .build());
        }

        List<OrderEntity> completedOrders = orderService.createCartsOrder(userPk, temporaryOrder, orderDTO, couponPk);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(completedOrders)
            .message("주문리스트에 주문 추가 성공")
            .build());
    }

    @PostMapping("/goods/{goodsPk}")
    public ResponseEntity<ResDTO> createGoodsOrder(@AuthenticationPrincipal String userPk,
                                                   @PathVariable Long goodsPk,
                                                   @RequestParam(required = false) Long couponPk,
                                                   @RequestParam Integer goodsQuantity,
                                                   @RequestBody OrderDTO orderDTO){

        OrderEntity completedOrder = orderService.createGoodsOrder(userPk, goodsPk, couponPk, orderDTO, goodsQuantity);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(completedOrder)
            .message("주문리스트에 주문 추가 성공")
            .build());
    }

    @Operation(summary = "주문 목록 조회", description = """
            인증/인가 : 로그인 필요\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
    @GetMapping
    public ResponseEntity<ResDTO> getOrderList(@AuthenticationPrincipal String userPk,
                               Model model){
        List<OrderEntity> orderList = orderService.getOrderList(userPk);
//        log.warn(orderList.toString());
//        model.addAttribute("orderRecipient", orderList.getOrderRecipient());
//        model.addAttribute("orderAddress", orderList.getOrderAddress());
//        model.addAttribute("orderPhone", orderList.getOrderPhone());
//        model.addAttribute("orderRequest", orderList.getOrderRequest());
//        model.addAttribute("orderList", orderList.getOrderList());
//        model.addAttribute("orderTotal", orderList.getOrderTotal());

//        return "order";}
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(orderList)
           .message("주문 리스트 조회 성공")
           .build());
    }

    @Operation(summary = "주문 상세 조회", description = """
            테스트 방법: 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            인증/인가 : 로그인 필요\s
            orderPk : 3\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
    @GetMapping("/{orderPk}")
    public ResponseEntity<ResDTO> getOrderGoods(@AuthenticationPrincipal String userPk,
                                                @PathVariable Long orderPk){
        GoodsEntity goods = orderService.getOrderGoods(userPk, orderPk);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(goods)
           .message("주문 상세 조회 성공")
           .build());
    }

}

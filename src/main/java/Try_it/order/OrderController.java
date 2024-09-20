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

    @Operation(summary = "장바구니에 담긴 전체 상품 주문 임시 저장 API", description = "토큰 필요/ * 세션에 정보를 저장했기에 스웨거페이지에서 직접적인 확인은 불가능하나, 토큰을 넣어 실행했을 경우" +
        " 해당 유저의 장바구니에 담긴 상품들이 임시 저장 성공했다는 메시지가 나오면 해당 API가 성공적으로 처리됐음을 알 수 있습니다.")
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

    @Operation(summary = "장바구니에 담긴 전체 상품 주문 API", description = "orderDTO : {\n" +
        "  \"orderRequest\": \"\b경비실에 맡겨주세요.\",\n" +
        "  \"orderRecipient\": \"추수연\",\n" +
        "  \"orderAddress\": \"서울시 서대문구 홍은동\",\n" +
        "  \"orderPhone\": \"1012345678\"\n" +
        "} / 쿠폰Pk(필수 아님), 토큰 필요")
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

        List<OrderEntity> completedOrders =orderService.createCartsOrder(userPk, temporaryOrder, orderDTO, couponPk);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(completedOrders)
            .message("주문리스트에 주문 추가 성공")
            .build());
    }

    @Operation(summary = "주문 목록 조회", description = "토큰 필요")
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

    @Operation(summary = "주문 상세 조회", description = "토큰, orderPk 필요")
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

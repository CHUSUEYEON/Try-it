package Try_it.order;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users/orders")
@Tag(name = "order", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 추가 API")
    @PostMapping("goods/{goodPk}")
    public ResponseEntity<ResDTO> createOrder(@AuthenticationPrincipal String userPk,
                                              @RequestParam(required = false) Long couponPk,
                                              @RequestBody OrderDTO orderDTO,
                                              @PathVariable Long goodPk){
        OrderEntity newOrder = orderService.createOrder(userPk, orderDTO, goodPk, couponPk);
        OrderDTO responseOrder = OrderDTO.builder()
            .orderPk(newOrder.getOrderPk())
            .orderCreatedAt(newOrder.getOrderCreatedAt())
            .orderRecipient(newOrder.getOrderRecipient())
            .orderTotal(newOrder.getOrderTotal())
            .orderPhone(newOrder.getOrderPhone())
            .orderAddress(newOrder.getOrderAddress())
            .orderRequest(newOrder.getOrderRequest())
            .orderIsCancelled(newOrder.getOrderIsCancelled())
            .user(newOrder.getUser().getUserPk())
            .coupon(couponPk != null ? newOrder.getCoupon().getCouponPk() : null)
            .build();
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(responseOrder)
           .message("주문리스트에 주문 추가 성공")
           .build());
    }

}

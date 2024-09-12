package Try_it.pay;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import Try_it.order.OrderDTO;
import Try_it.order.OrderEntity;
import Try_it.order.OrderService;
import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PayController{
    private final PayService payService;
    private final OrderService orderService;


    @Autowired
    public PayController(PayService payService, OrderService orderService) {
        this.payService = payService;
        this.orderService = orderService;
    }

    public ResponseEntity<ResDTO> createPayment(@AuthenticationPrincipal String userPk,
                                                @RequestParam(required = false) Long couponPk,
                                                @RequestBody OrderDTO orderDTO,
                                                @PathVariable Long goodPk){
        OrderEntity orderEntity = orderService.createOrder(userPk, orderDTO, goodPk, couponPk);
        return ResponseEntity.ok(ResDTO.builder()
                .statusCode(StatusCode.OK)
                .data(orderEntity)
                .message("success")
            .build());
    }
}

package Try_it.pay;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import Try_it.order.OrderDTO;
import Try_it.order.OrderEntity;
import Try_it.order.OrderService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
public class PayController{
    private final PayService payService;
    private final OrderService orderService;
    private IamportClient iamportClient;

    @Value("${imp.api.key}")
    private String apiKey;
    @Value("${imp.api.secretkey}")
    private String secretKey;

    @Autowired
    public PayController(PayService payService, OrderService orderService) {
        this.payService = payService;
        this.orderService = orderService;
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    @Operation(summary = "결제 API")
    @PostMapping("/order/payment/{imp_uid}")
    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid, @RequestBody OrderDTO orderDTO,
                                                    @AuthenticationPrincipal String userPk)
        throws IamportResponseException, IOException{
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);

        log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse().getMerchantUid());

        payService.processPaymentDone(orderDTO, userPk);

        return payment;
    }
}

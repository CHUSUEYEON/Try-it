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
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "pay", description = "결제 관련 API")
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

//    @Operation(summary = "결제 API", description = "현재 진행 중인 API 입니다.")
//    @PostMapping("/order/payment/{imp_uid}")
//    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid, @RequestBody PaymentRequestDTO paymentRequestDTO,
//                                                    @AuthenticationPrincipal String userPk)
//        throws IamportResponseException, IOException{
//        log.info("결제 들어갔니?");
//        log.info("imp_uid : {}", imp_uid);
//        log.info("paymentRequestDTO : {}", paymentRequestDTO);
//        log.info("iamportClient : {}", iamportClient.paymentByImpUid(imp_uid).toString());
//        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
//        log.info("payment: {}", payment);
//
//        log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse().getMerchantUid());
//
//        payService.processPaymentDone(paymentRequestDTO, userPk);
//
//        return payment;
//    }

    @Operation(summary = "결제 API", description = """
            테스트 방법: 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            인증/인가 : 로그인 필요\s
            \s
            { "imp_uid": "imp_786542026571", "order": 1, "goods" : 4}\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
    @PostMapping("/order/payment")
    public ResponseEntity<ResDTO> createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO,
                                                @AuthenticationPrincipal String userPk)
    throws IamportResponseException, IOException {
        log.info("imp_uid : {}", paymentRequestDTO.getImp_uid());
        log.info("merchant_uid : {}", paymentRequestDTO.getMerchant_uid());
        log.info("paymentRequestDTO : {}", paymentRequestDTO);

//        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(paymentRequestDTO.getImp_uid());
//
//        log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getMessage());

//        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
//        log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse().getMerchantUid());

        payService.processPaymentDone(paymentRequestDTO, userPk);

        return ResponseEntity.ok().body(ResDTO.builder()
               .statusCode(StatusCode.OK)
               .message("결제 성공")
               .build());
    }
}

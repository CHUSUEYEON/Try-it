//package Try_it.pay;
//
//import Try_it.common.dto.ResDTO;
//import com.siot.IamportRestClient.IamportClient;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//@Slf4j
//public class PayController {
//    private final PayService payService;
////    private IamportClient iamportClient;
//
////    @Value("${imp.api.key}")
////    private String apiKey;
////    @Value("${imp.api.secret}")
////    private String secretKey;
//
//    @Autowired
//    public PayController(PayService payService) {
//        this.payService = payService;
//    }
//
////    public void init(){
////        this.iamportClient = new IamportClient(apiKey, secretKey);
////    }
//
////    @PostMapping("/orders/payment")
////    public ResponseEntity<ResDTO> completePayment(@Login )
//}

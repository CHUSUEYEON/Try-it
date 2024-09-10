//package Try_it.pay;
//
//import Try_it.common.dto.ResDTO;
//import Try_it.order.OrderDTO;
//import Try_it.order.OrderEntity;
//import com.siot.IamportRestClient.IamportClient;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@Slf4j
//public class PayController extends PayService {
//    private final PayService payService;
//
//
//    @Autowired
//    public PayController(PayService payService) {
//        super();
//        this.payService = payService;
//    }
//
//    public ResponseEntity<ResDTO> createPayment(@RequestBody OrderDTO orderDTO){
//        PaymentDTO paymentDTO = orderService.createOrder;
//    }
//}

//package Try_it.pay;
//
//import com.siot.IamportRestClient.IamportClient;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class PayService {
//    private IamportClient api;
//    private final PayRepository payRepository;
//
//    @Value("${imp.api.key}")
//    private String apiKey;
//    @Value("${imp.api.secret}")
//    private String secretKey;
//
//
//    @Autowired
//    public PayService(PayRepository payRepository){
//        this.payRepository = payRepository;
//        this.api = new IamportClient(apiKey, secretKey);
//    }
//}

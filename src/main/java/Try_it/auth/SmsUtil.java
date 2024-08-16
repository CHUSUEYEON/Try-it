//package Try_it.auth;
//
//import jakarta.annotation.PostConstruct;
//import net.nurigo.sdk.NurigoApp;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SmsUtil {
//    @Value("${coolsms.api.key}")
//    private String apiKey;
//    @Value("${coolsms.api.secret}")
//    private String apiSecretKey;
//
//    final private AuthService authService;
//
//    public SmsUtil(AuthService authService) {
//        this.authService = authService;
//    }
//
//    @PostConstruct
//    private void init(){
//        this.authService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
//    }
//
//    // 단일 메시지 발송 예제
//    public SingleMessageSentResponse sendOne(String to, String verificationCode) {
//        Message message = new Message();
//        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
//        message.setFrom("발신번호 입력");
//        message.setTo(to);
//        message.setText("[Moyiza] 아래의 인증번호를 입력해주세요\n" + verificationCode);
//
//        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
//        return response;
//    }
//}

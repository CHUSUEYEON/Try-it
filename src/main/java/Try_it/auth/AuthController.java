package Try_it.auth;

import Try_it.common.dto.ResDTO;
import Try_it.security.TokenProvider;
import Try_it.common.vo.StatusCode;
import Try_it.user.UserDTO;
import Try_it.user.UserEntity;
import Try_it.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "로그인, 회원가입 관련 API(인가X)")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Value("${coolsms.api.key}")
    private String apikey;
    @Value("${coolsms.api.secret}")
    private String apiSecret;
    @Value(("${sender.phone}"))
    private String senderPhone;


    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private DefaultMessageService messageService;

    @Autowired
    public AuthController(AuthService authService, BCryptPasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apikey, apiSecret, "https://api.coolsms.co.kr");
    }


    @Operation(summary = "회원가입", description = " requestBody : 아이디, 닉네임, 패스워드, 이름, 주소, 성별, 연락처, 이메일 ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/register")
    public ResponseEntity<ResDTO> register(@Valid @RequestBody UserDTO userDTO) {

        UserEntity user = UserEntity.builder()
            .userId(userDTO.getUserId())
            .userName(userDTO.getUserName())
            .userPassword(passwordEncoder.encode(userDTO.getUserPassword()))
            .userEmail(userDTO.getUserEmail())
            .userAddress(userDTO.getUserAddress())
            .userGender(userDTO.getUserGender())
            .userPhone(userDTO.getUserPhone())
            .userCreatedAt(userDTO.getUserCreatedAt())
            .userIsAdmin(false)
            .build();

        UserEntity registeredUser = authService.createUser(user);
        UserDTO responseUserDTO = userDTO.builder()
            .userId(registeredUser.getUserId())
            .userName(registeredUser.getUserName())
            .userIdx(registeredUser.getUserIdx())
            .userAddress(registeredUser.getUserAddress())
            .userGender(registeredUser.getUserGender())
            .userPhone(registeredUser.getUserPhone())
            .userCreatedAt(registeredUser.getUserCreatedAt())
            .userUpdatedAt(registeredUser.getUserUpdatedAt())
            .userDeletedAt(registeredUser.getUserDeletedAt())
            .userEmail(registeredUser.getUserEmail())
            .userIsAdmin(registeredUser.getUserIsAdmin())
            .build();

        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(responseUserDTO)
            .message("회원가입 성공")
            .build()
        );
    }

    @Operation(summary = "로그인", description = "requestBody : 아이디, 패스워드")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ResDTO> login(@Valid @RequestBody UserDTO userDTO){
        UserEntity user = authService.getUserByCredentials(userDTO.getUserId(), userDTO.getUserPassword());
        if(user != null){
            String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                .userId(user.getUserId())
                .userIdx(user.getUserIdx())
                .userName(user.getUserName())
                .userAddress(user.getUserAddress())
                .userGender(user.getUserGender())
                .userPhone(user.getUserPhone())
                .userCreatedAt(user.getUserCreatedAt())
                .userUpdatedAt(user.getUserUpdatedAt())
                .userDeletedAt(user.getUserDeletedAt())
                .userEmail(user.getUserEmail())
                .userIsAdmin(user.getUserIsAdmin())
                .token(token)
               .build();

            return ResponseEntity.ok().body(ResDTO
               .builder()
               .statusCode(StatusCode.OK)
               .data(responseUserDTO)
               .message("로그인 성공")
                .build());
        }else {
            return ResponseEntity
                .badRequest()
               .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).build());
        }
    }

    @Operation(summary = "메일 인증코드 보내기", description = "requestBody : 이메일(인가X)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 코드 전송 성공"),
        @ApiResponse(responseCode = "400", description = "인증 코드 전송 실패")
    })
    @PostMapping("/email")
    public ResponseEntity<ResDTO> sendEmail(@Valid @RequestBody VerificationDTO verificationDTO){
        LocalDateTime verifiedAt = LocalDateTime.now();
        authService.sendVerificationCode(verificationDTO.getEmail(), verifiedAt);
        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(verificationDTO.getEmail())
            .message("인증 코드 전송 성공")
            .build()
        );
    }

    @Operation(summary = "메일 인증", description = "requestBody : 인증 코드(인가X)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 성공"),
        @ApiResponse(responseCode = "400", description = "인증 실패")
    })
    @PostMapping("/verify-email")
    public ResponseEntity<ResDTO> verifyEmail(@RequestBody VerificationDTO verificationDTO){
        LocalDateTime verifiedAt = LocalDateTime.now();
        authService.verifyCode(verificationDTO.getCode(), verifiedAt);
        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .message("메일 인증 성공")
            .build()
        );
    }

    @Operation(summary = "핸드폰 인증 코드 인증", description = "requestBody : phone 번호(인가X)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메시지 전송 성공"),
        @ApiResponse(responseCode = "400", description = "메시지 전송 실패")
    })
    @PostMapping("/phone")
    public ResponseEntity<ResDTO> sendMessage(@Valid @RequestBody VerificationDTO verificationDTO)throws IOException{

        LocalDateTime verifiedAt = LocalDateTime.now();
        VerificationCode code = authService.sendPhoneVerificationCode(verifiedAt);

        Message message = new Message();
        message.setFrom(senderPhone);
        message.setTo(verificationDTO.getPhone());
        message.setText(code.getCode());

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(response)
            .message("메시지 전송 성공")
            .build()
        );
    }

    @Operation(summary = "핸드폰 인증", description = "RequestBody : code(인가X)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "핸드폰 인증 성공"),
        @ApiResponse(responseCode = "400", description = "핸드폰 인증 실패")
    })
    @PostMapping("/verify-phone")
    public ResponseEntity<ResDTO> verifyPhone(@RequestBody VerificationDTO verificationDTO){
        LocalDateTime verifiedAt = LocalDateTime.now();
        authService.verifyCode(verificationDTO.getCode(), verifiedAt);
        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .message("핸드폰 인증 성공")
            .build()
        );
    }
}

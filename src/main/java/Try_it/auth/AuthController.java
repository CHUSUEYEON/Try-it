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
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "로그인, 회원가입 관련 API(인가X)")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthService authService, BCryptPasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
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

    @Operation(summary = "인증코드 보내기", description = "requestBody : 이메일(인가X)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 코드 전송 성공"),
        @ApiResponse(responseCode = "400", description = "인증 코드 전송 실패")
    })
    @PostMapping("/email")
    public ResponseEntity<ResDTO> sendEmail(@Valid @RequestBody EmailDTO emailDTO){
        LocalDateTime verifiedAt = LocalDateTime.now();
        authService.sendVerificationCode(emailDTO.getEmail(), verifiedAt);
        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(emailDTO.getEmail())
            .message("인증 코드 전송 성공")
            .build()
        );
    }

    @Operation(summary = "메일 인증", description = "requsetBody : 인증 코드(인가X)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 성공"),
        @ApiResponse(responseCode = "400", description = "인증 실패")
    })
    @PostMapping("/verify-email")
    public ResponseEntity<ResDTO> verifyEmail(@RequestBody EmailDTO emailDTO){
        LocalDateTime verifiedAt = LocalDateTime.now();
        authService.verifyCode(emailDTO.getCode(), verifiedAt);
        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .message("메일 인증 성공")
            .build()
        );
    }
}

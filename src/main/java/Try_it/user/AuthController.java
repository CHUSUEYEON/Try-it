package Try_it.user;

import Try_it.common.dto.ResDTO;
import Try_it.security.TokenProvider;
import Try_it.common.vo.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "로그인, 회원가입 관련 API(인가X)")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthController(UserService userService, BCryptPasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.userService = userService;
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

        UserEntity registeredUser = userService.createUser(user);
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
        UserEntity user = userService.getUserByCredentials(userDTO.getUserId(), userDTO.getUserPassword());
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
}

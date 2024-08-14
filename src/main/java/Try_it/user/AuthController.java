package Try_it.user;

import Try_it.dto.ResDTO;
import Try_it.security.TokenProvider;
import Try_it.vo.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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

    @Operation(summary = "회원가입", description = " requestBody : 아이디, 닉네임, 패스워드, 이름, 주소, 성별, 연락처, 이메일 입력 ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/register")
    public ResponseEntity<ResDTO> registerUser(@RequestBody UserDTO userDTO) {

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
}

package Try_it.user;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/user")
@Tag(name = "User", description = "회원 관련 API")
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원 탈퇴", description = "requestBody : 패스워드, authentication : 토큰 필요(로그인 상태)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
        @ApiResponse(responseCode = "400", description = "회원 탈퇴 실패")
    })
    @DeleteMapping("")
    public ResponseEntity<ResDTO> deleteUser(@Valid @RequestBody UserDTO userDTO,
                                             @AuthenticationPrincipal String userIdx
    ){
        UserEntity user = userService.deleteUser(Long.valueOf(userIdx), userDTO.getUserPassword());

        final UserDTO responseUserDTO = UserDTO.builder()
            .userIdx(user.getUserIdx())
            .userName(user.getUserName())
            .userId(user.getUserId())
            .userCreatedAt(user.getUserCreatedAt())
            .userUpdatedAt(user.getUserUpdatedAt())
            .userDeletedAt(Timestamp.valueOf(LocalDateTime.now()))
            .build();

        return ResponseEntity.ok().body(ResDTO.builder()
               .statusCode(StatusCode.OK)
               .data(responseUserDTO)
               .message("회원탈퇴 성공")
               .build());
    }
}

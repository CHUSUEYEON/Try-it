package Try_it.user;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import Try_it.favorites.FavoritesEntity;
import Try_it.favorites.FavoritesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "User", description = "회원 관련 API")
public class UserController {

    final private UserService userService;
    final private FavoritesService favoritesService;

    public UserController(UserService userService, FavoritesService favoritesService) {
        this.userService = userService;
        this.favoritesService = favoritesService;
    }

    @Operation(summary = "회원 탈퇴", description = """
            테스트 방법 : 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            인증/인가 : 탈퇴 원하는 유저로 로그인 필요\s
            \s
            { "userPassword": "qwer1234!" }\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
    @DeleteMapping("")
    public ResponseEntity<ResDTO> deleteUser(@Valid @RequestBody UserDTO userDTO,
                                             @AuthenticationPrincipal String userPk
    ){
        UserEntity user = userService.deleteUser(Long.valueOf(userPk), userDTO.getUserPassword());

        final UserDTO responseUserDTO = UserDTO.builder()
            .userPk(user.getUserPk())
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

    @Operation(summary = "찜 목록 조회", description = """
            인증/인가 : 로그인 필요\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
    @GetMapping("/favorites")
    public ResponseEntity<ResDTO> getFavorites(@AuthenticationPrincipal String userPk){
        List<FavoritesEntity> getFavorites = favoritesService.get(userPk);
        List<FavoritesEntity> allFavorites = new ArrayList<>();
        allFavorites.addAll(getFavorites);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(allFavorites)
            .message("찜 조회 성공")
            .build());
    }


}

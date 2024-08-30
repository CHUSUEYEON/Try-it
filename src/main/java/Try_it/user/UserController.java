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
@RequestMapping(value = "/user")
@Tag(name = "User", description = "회원 관련 API")
public class UserController {

    final private UserService userService;
    final private FavoritesService favoritesService;

    public UserController(UserService userService, FavoritesService favoritesService) {
        this.userService = userService;
        this.favoritesService = favoritesService;
    }

    @Operation(summary = "회원 탈퇴", description = "requestBody : 패스워드, authentication : 토큰 필요(로그인 상태)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
        @ApiResponse(responseCode = "400", description = "회원 탈퇴 실패")
    })
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

    @Operation(summary = "찜 목록 조회", description = "토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "찜 조회 성공"),
        @ApiResponse(responseCode = "400", description = "찜 조회 실패")
    })
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
    //..

}

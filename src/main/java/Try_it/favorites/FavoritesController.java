package Try_it.favorites;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/favorites")
@Tag(name = "favorites", description = "찜 관련 API")
public class FavoritesController {
    final private FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @Operation(summary = "찜 추가", description = """
            goodsPk : 4  \s
            인증/인가 : 로그인 필요 \s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
    @PostMapping("/{goodsPk}")
    public ResponseEntity<ResDTO> addFavorite(@AuthenticationPrincipal String userPk,
                                              @PathVariable Long goodsPk){
        FavoritesEntity favorites = favoritesService.create(userPk, goodsPk);

        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(favorites)
           .message("찜 추가 성공")
           .build());
    }


    @Operation(summary = "찜 삭제", description = """
            favPk : 1  \s
            인증/인가 : 로그인 필요 \s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
    @DeleteMapping("/{favPk}")
    public ResponseEntity<ResDTO> removeFavorite(@AuthenticationPrincipal String userPk,
                               @PathVariable Long favPk) {
        FavoritesEntity deletedFavorites = favoritesService.delete(userPk, favPk);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(deletedFavorites)
            .message("찜 삭제 성공")
            .build());
    }
}

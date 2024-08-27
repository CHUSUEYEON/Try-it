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

    @Operation(summary = "찜 추가", description = " path : 상품Pk / 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "찜 등록 성공"),
        @ApiResponse(responseCode = "400", description = "찜 등록 실패")
    })
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


    @Operation(summary = "찜 삭제", description = "path : 찜Pk / 해당 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "찜 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "찜 삭제 실패")
    })
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

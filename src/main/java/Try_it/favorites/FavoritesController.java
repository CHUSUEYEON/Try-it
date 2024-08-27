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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/favorites")
@Tag(name = "favorites", description = "찜 관련 API")
public class FavoritesController {
    final private FavoritesService favoriteService;

    public FavoritesController(FavoritesService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Operation(summary = "찜 추가", description = " path : 상품Pk / 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "찜 등록 성공"),
        @ApiResponse(responseCode = "400", description = "찜 등록 실패")
    })
    @PostMapping("/{goodsPk}")
    public ResponseEntity<ResDTO> addFavorite(@AuthenticationPrincipal String userPk,
                                              @PathVariable Long goodsPk){
        FavoritesEntity favorites = favoriteService.create(userPk, goodsPk);

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
        FavoritesEntity deletedFavorites = favoriteService.delete(userPk, favPk);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(deletedFavorites)
            .message("찜 삭제 성공")
            .build());
    }

    @Operation(summary = "찜 조회", description = "토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "찜 조회 성공"),
        @ApiResponse(responseCode = "400", description = "찜 조회 실패")
    })
    @GetMapping("")
    public ResponseEntity<ResDTO> getFavorites(@AuthenticationPrincipal String userPk){
        List<FavoritesEntity> getFavorites = favoriteService.get(userPk);
        List<FavoritesEntity> allFavorites = new ArrayList<>();
        allFavorites.addAll(getFavorites);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(allFavorites)
           .message("찜 조회 성공")
           .build());
    }

}

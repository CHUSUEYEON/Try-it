package Try_it.favorites;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/favorites")
@Tag(name = "favorites", description = "찜 관련 API")
public class FavoritesController {
    final private FavoritesService favoriteService;

    public FavoritesController(FavoritesService favoriteService) {
        this.favoriteService = favoriteService;
    }

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
}

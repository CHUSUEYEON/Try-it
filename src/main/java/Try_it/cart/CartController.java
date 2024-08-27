package Try_it.cart;

import Try_it.common.dto.ResDTO;
import Try_it.user.UserDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.POST;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "장바구니 관련 API")
@Slf4j
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @POST("/{goodsPk}")
    public ResponseEntity<ResDTO> addCart(@AuthenticationPrincipal String userPk,
                                          @RequestBody CartDTO cartDTO,
                                          @PathVariable Long goodsPk){
        CartEntity addCart = cartService.createCart(userPk, cartDTO, goodsPk);
        return null;
    }
}

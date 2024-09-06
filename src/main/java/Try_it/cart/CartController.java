package Try_it.cart;

import Try_it.common.dto.ResDTO;
import Try_it.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.POST;

import java.util.List;

@RestController
@RequestMapping("/users/carts")
@Tag(name = "Cart", description = "장바구니 관련 API")
@Slf4j
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "장바구니에 상품 추가")
    @PostMapping("/goods/{goodsPk}")
    public ResponseEntity<ResDTO> addCart(@AuthenticationPrincipal String userPk,
                                          @PathVariable Long goodsPk){
        CartEntity addCart = cartService.createCart(userPk, goodsPk);
        CartDTO responseCart = CartDTO.builder()
            .cartPk(addCart.getCartPk())
            .cartAmount(addCart.getCartAmount())
            .cartIsPaid(addCart.getCartIsPaid())
            .goods(addCart.getGoods().getGoodsPk())
            .user(addCart.getUser().getUserPk())
            .build();

        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(200)
           .data(responseCart)
           .message("장바구니에 상품 추가 성공")
           .build());
    }

    @Operation(summary = "장바구니에 있는 상품 삭제")
    @DeleteMapping("/goods/{goodsPk}")
    public ResponseEntity<ResDTO> deleteGoodsInCart(@AuthenticationPrincipal String userPk,
                                                    @PathVariable Long goodsPk) {
        cartService.deleteGoodsInCart(userPk, goodsPk);

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(200)
            .message("장바구니에서 " + goodsPk + "번 상품 삭제 성공")
            .build());
    }

    @Operation(summary = "장바구니에 있는 상품 삭제")
    @DeleteMapping("")
    public ResponseEntity<ResDTO> deleteCart(@AuthenticationPrincipal String userPk) {
        cartService.deleteCart(userPk);

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(200)
            .message("전체 장바구니 삭제 성공")
            .build());
    }

}

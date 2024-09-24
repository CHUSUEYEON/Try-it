package Try_it.cart;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import Try_it.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.POST;

import java.util.List;

import static java.time.LocalTime.now;

@RestController
@RequestMapping("/carts")
@Tag(name = "Cart", description = "장바구니 관련 API")
@Slf4j
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "장바구니에 상품 추가", description = """
            테스트 방법 : 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            goodsPk : 3
            인증/인가 : 로그인 필요\s
            \s
            { "cartAmount": 3 }\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
   )
    @PostMapping("/goods/{goodsPk}")
    public ResponseEntity<ResDTO> addCart(@AuthenticationPrincipal String userPk,
                                          @PathVariable Long goodsPk,
                                          @RequestBody CartDTO cartDTO){
        CartEntity addCart = cartService.createCart(userPk, goodsPk, cartDTO);
        CartDTO responseCart = CartDTO.builder()
            .cartPk(addCart.getCartPk())
            .cartAmount(addCart.getCartAmount())
            .cartIsPaid(addCart.getCartIsPaid())
            .goods(addCart.getGoods().getGoodsPk())
            .user(addCart.getUser().getUserPk())
            .cartCreatedAt(addCart.getCartCreatedAt())
            .build();

        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(responseCart)
           .message("장바구니에 상품 추가 성공")
           .build());
    }

    @Operation(summary = "장바구니에 있는 상품 삭제", description = """
            테스트 방법 : 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            goodsPk : 3
            인증/인가 : 로그인 필요\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
    @DeleteMapping("/goods/{goodsPk}")
    public ResponseEntity<ResDTO> deleteGoodsInCart(@AuthenticationPrincipal String userPk,
                                                    @PathVariable Long goodsPk) {
        cartService.deleteGoodsInCart(userPk, goodsPk);

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(200)
            .message("장바구니에서 " + goodsPk + "번 상품 삭제 성공")
            .build());
    }

    @Operation(summary = "장바구니에 있는 전체 상품 삭제", description = """
            인증/인가 : 로그인 필요\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
    @DeleteMapping("")
    public ResponseEntity<ResDTO> deleteCart(@AuthenticationPrincipal String userPk) {
        cartService.deleteCart(userPk);

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .message("전체 장바구니 삭제 성공")
            .build());
    }

    @Operation(summary = "장바구니에 있는 상품 수량 변경", description = """
            테스트 방법 : 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            cartPk : 3
            인증/인가 : 로그인 필요\s
            \s
            { "cartAmount": 5 }\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
    @PatchMapping("/{cartPk}")
    public ResponseEntity<ResDTO> updateCart(@AuthenticationPrincipal String userPk,
                                             @RequestBody CartDTO cartDTO,
                                             @PathVariable Long cartPk){
        CartEntity updatedCart = cartService.updateCart(userPk, cartDTO, cartPk);
            return ResponseEntity.ok().body(ResDTO.builder()
                    .data(updatedCart)
                    .message("수량 변경 성공")
                    .statusCode(StatusCode.OK)
                .build());
        }


    @Operation(summary = "장바구니 조회", description = """
            인증/인가 : 로그인 필요\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
    @GetMapping
    public ResponseEntity<ResDTO<Object>> getCarts(@AuthenticationPrincipal String userPk,
                                           @RequestParam(value = "page", defaultValue = "0") Integer page,
                                           @RequestParam(value = "sort", defaultValue = "cartCreatedAt") String sort,
                                           @RequestParam(value = "direction", defaultValue = "ASC") String direction
                                           ){
        Page<CartEntity> carts = cartService.getCarts(page, sort, direction, userPk);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(carts)
           .message("장바구니 목록 조회 성공")
           .build());
    }



}

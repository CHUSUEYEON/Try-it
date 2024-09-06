package Try_it.cart;

import Try_it.goods.GoodsEntity;
import Try_it.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "장바구니 정보")
public class CartDTO {
    @Schema(description = "장바구니 인덱스", example = "1")
    private Long cartPk;

    @Schema(description = "상품 총 개수", example = "5")
    private Integer cartAmount;

    @Schema(description = "결제 여부", example = "true")
    private Boolean cartIsPaid;

    @Schema(description = "상품 추가 날짜", example = "2024-02-24")
    private Timestamp cartCreatedAt;

    @Schema(description = "해당 상품")
    private Long goods;

    @Schema(description = "해당 유저")
    private Long user;

}

package Try_it.coupon;

import Try_it.order.OrderEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "쿠폰 정보")
public class CouponDTO {
    @Schema(description = "쿠폰 인덱스", example = "1")
    private Long couponPk;

    @Schema(description = "쿠폰 이름", example = "추석 특집 할인")
    private String couponName;

    @Schema(description = "쿠폰 할인율", example = "10")
    private Integer couponDiscountRate;

    @Schema(description = "쿠폰 할인되는 가격", example = "1000")
    private Integer couponPrice;

    @Schema(description = "쿠폰 코드", example = "SUDFJE")
    private String couponCode;

    @Schema(description = "쿠폰 마감 기한", example = "2024년 11월 10일")
    private String couponDeadline;

    @Schema(description = "쿠폰 내용", example = "추석 특집으로 모든 회원들게 10퍼센트 할인 쿠폰 드립니다.")
    private String couponContent;
}

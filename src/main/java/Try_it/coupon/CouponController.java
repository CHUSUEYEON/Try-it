package Try_it.coupon;

import Try_it.alarm.AlarmDTO;
import Try_it.alarm.AlarmEntity;
import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/coupons")
@Tag(name = "Coupons", description = "쿠폰 관련 api")
@Slf4j
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @Operation(summary = "쿠폰 생성 api", description = " couponDTO : {\n" +
        "  \"couponName\": \"추석 특집 할인\",\n" +
        "  \"couponDiscountRate\": 10,\n" +
        "  \"couponDeadline\": \"2024년 11월 10일\",\n" +
        "  \"couponContent\": \"추석 특집으로 모든 회원들게 10퍼센트 할인 쿠폰 드립니다.\"\n" +
        "} / 관리자 토큰 필요")
    @PostMapping
    public ResponseEntity<ResDTO> createCoupon(@AuthenticationPrincipal String userPk,
                                               @RequestBody CouponDTO couponDTO
                                               ){
        CouponEntity createdCoupon = couponService.createCoupon(couponDTO, userPk);
        log.info("Created coupon: {}", createdCoupon);
        CouponDTO responseCoupon = couponDTO.builder()
            .couponPk(createdCoupon.getCouponPk())
            .couponName(createdCoupon.getCouponName())
            .couponCode(createdCoupon.getCouponCode())
            .couponPrice(createdCoupon.getCouponPrice())
            .couponContent(createdCoupon.getCouponContent())
            .couponDeadline(createdCoupon.getCouponDeadline())
            .couponDiscountRate(createdCoupon.getCouponDiscountRate())
            .build();

        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(responseCoupon)
           .message("쿠폰 생성 성공")
           .build());
    }

    @Operation(summary = "쿠폰 전송 api", description = "alarmDTO : {\n" +
        "  \"alarmContent\": \"알림입니다.\",\n" +
        "  \"alarmTitle\": \"알림 제목\"\n" +
        "} / 쿠폰 Pk, 관리자 토큰 필요")
    @PostMapping("/{couponPk}")
    public ResponseEntity<ResDTO> sendCoupon(@AuthenticationPrincipal String userPk,
                                             @RequestBody AlarmDTO alarmDTO,
                                             @PathVariable Long couponPk){
        List<AlarmEntity> result = couponService.sendCoupon(couponPk, alarmDTO, userPk);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(result)
           .message("쿠폰 전송 성공")
           .build());
    }

}

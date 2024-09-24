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

    @Operation(summary = "쿠폰 생성 api", description = """
            테스트 방법: 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            인증/인가 : 관리자 로그인 필요\s
            \s
            { "couponName": "추석 특집 할인", "couponDiscountRate": 10, "couponDeadline": "2024년 11월 10일", "couponContent": "추석 특집으로 모든 회원들게 10퍼센트 할인 쿠폰 드립니다." }\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
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

    @Operation(summary = "쿠폰 전송 api", description = """
            테스트 방법 : 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            couponPk : 3
            인증/인가 : 관리자 로그인 필요\s
            \s
            { "alarmContent": "알림입니다.", "alarmTitle": "알림 제목" }\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
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

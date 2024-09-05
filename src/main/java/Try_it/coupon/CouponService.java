package Try_it.coupon;

import Try_it.alarm.AlarmDTO;
import Try_it.alarm.AlarmEntity;
import Try_it.alarm.AlarmService;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    @Autowired
    public CouponService(CouponRepository couponRepository, UserRepository userRepository, AlarmService alarmService) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
        this.alarmService = alarmService;
    }


    public CouponEntity createCoupon(final CouponDTO couponDTO, final String userPk) {
        userRepository.findAdminByUserPk(Long.valueOf(userPk))
            .orElseThrow(()-> new IllegalArgumentException("관리자로 로그인 해주세요."));

        CouponEntity newCoupon = CouponEntity.builder()
            .couponCode(couponDTO.getCouponCode())
            .couponContent(couponDTO.getCouponContent())
            .couponPrice(couponDTO.getCouponPrice())
            .couponDeadline(couponDTO.getCouponDeadline())
            .couponName(couponDTO.getCouponName())
            .couponDiscountRate(couponDTO.getCouponDiscountRate())
            .build();

        return couponRepository.save(newCoupon);
    }

    public List<AlarmEntity> sendCoupon(final Long couponPk,
                                        final AlarmDTO alarmDTO,
                                        final String userPk){
        userRepository.findAdminByUserPk(Long.valueOf(userPk))
            .orElseThrow(()->new IllegalArgumentException("관리자로 로그인 해주세요."));
        CouponEntity coupon = couponRepository.findById(couponPk).orElseThrow(()
            -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

        List<UserEntity> users = userRepository.findAll();
        log.info("Users: {}", users.size());
        log.info("Users: {}", users);
        List<AlarmEntity> result = List.of();
        for(UserEntity user : users){
            if(user.getUserIsAdmin() == false) {
                alarmService.send(user.getUserPk(), alarmDTO.getAlarmTitle(), alarmDTO.getAlarmContent(), coupon.getCouponCode());
                log.info("user: {} ", user.getUserPk());
                result = alarmService.getAlarmsList(String.valueOf(user.getUserPk()));
            }
        }
        return result;
    }
}

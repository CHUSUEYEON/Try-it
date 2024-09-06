package Try_it.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponUserMappingRepository extends JpaRepository<CouponUserMappingEntity, Long> {
    Optional<CouponUserMappingEntity> findByUser_UserPkAndCoupon_CouponPk(Long userPk, Long couponPk);
}

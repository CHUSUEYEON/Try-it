package Try_it.order;

import Try_it.coupon.CouponEntity;
import Try_it.coupon.CouponRepository;
import Try_it.coupon.CouponUserMappingEntity;
import Try_it.coupon.CouponUserMappingRepository;
import Try_it.goods.GoodsEntity;
import Try_it.goods.GoodsRepository;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderListRepository orderListRepository;
    private final GoodsRepository goodsRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CouponUserMappingRepository couponUserMappingRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderListRepository orderListRepository, GoodsRepository goodsRepository, UserRepository userRepository, CouponRepository couponRepository, CouponUserMappingRepository couponUserMappingRepository) {
        this.orderRepository = orderRepository;
        this.orderListRepository = orderListRepository;
        this.goodsRepository = goodsRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.couponUserMappingRepository = couponUserMappingRepository;
    }

    public OrderEntity createOrder(final String userPk,
                                   final OrderDTO orderDTO,
                                   final Long goodPk,
                                   final Long couponPk){
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new IllegalArgumentException("로그인을 해주세요."));

        GoodsEntity goods = goodsRepository.findById(goodPk)
            .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 상품입니다. "));

        CouponEntity coupon = null;
        CouponUserMappingEntity mapping = null;
        if (couponPk != null) {
            coupon = couponRepository.findById(couponPk)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰이 없습니다."));
            mapping = couponUserMappingRepository.findByUser_UserPkAndCoupon_CouponPk(Long.valueOf(userPk), coupon.getCouponPk())
                .orElseThrow(()-> new IllegalArgumentException("사용 가능한 쿠폰이 없습니다."));
        }

        OrderEntity savedOrder = OrderEntity.builder()
            .orderAddress(orderDTO.getOrderAddress())
            .orderPhone(orderDTO.getOrderPhone())
            .orderIsCancelled(false)
            .coupon(mapping != null ? mapping.getCoupon() : null)
            .orderRequest(orderDTO.getOrderRequest())
            .orderTotal(orderDTO.getOrderTotal())
            .orderRecipient(orderDTO.getOrderRecipient())
            .user(user)
            .build();

        orderRepository.save(savedOrder);

        OrderListEntity orderList = OrderListEntity.builder()
            .order(savedOrder)
            .goods(goods)
            .build();

        orderListRepository.save(orderList);

        return savedOrder;
    }
}

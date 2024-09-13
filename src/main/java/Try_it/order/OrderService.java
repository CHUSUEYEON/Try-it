package Try_it.order;

import Try_it.cart.CartEntity;
import Try_it.cart.CartRepository;
import Try_it.coupon.CouponEntity;
import Try_it.coupon.CouponRepository;
import Try_it.coupon.CouponUserMappingEntity;
import Try_it.coupon.CouponUserMappingRepository;
import Try_it.goods.GoodsEntity;
import Try_it.goods.GoodsRepository;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderListRepository orderListRepository;
    private final GoodsRepository goodsRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CouponUserMappingRepository couponUserMappingRepository;
    private final CartRepository cartRepository;
    private final HttpSession httpSession;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderListRepository orderListRepository, GoodsRepository goodsRepository, UserRepository userRepository, CouponRepository couponRepository, CouponUserMappingRepository couponUserMappingRepository, CartRepository cartRepository, HttpSession httpSession) {
        this.orderRepository = orderRepository;
        this.orderListRepository = orderListRepository;
        this.goodsRepository = goodsRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.couponUserMappingRepository = couponUserMappingRepository;
        this.cartRepository = cartRepository;
        this.httpSession = httpSession;
    }


    public OrderEntity createOrder(final String userPk,
                                   final OrderDTO orderDTO,
                                   final Long goodPk,
                                   final Long couponPk){
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new IllegalArgumentException("로그인을 해주세요."));

        GoodsEntity goods = goodsRepository.findById(goodPk)
            .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 상품입니다. "));

        CartEntity cart = cartRepository.findByUser_UserPkAndGoods_GoodsPk(Long.valueOf(userPk), goodPk);

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
            .orderTotal(goods.getGoodsPrice() * cart.getCartAmount())
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

    public List<OrderDTO> createCartsTempOrder(final String userPk) {
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new IllegalArgumentException("로그인을 해주세요."));

        List<CartEntity> carts = cartRepository.findAllByUser_userPk(Long.valueOf(userPk));

        if(carts.isEmpty()){
            throw new IllegalArgumentException("장바구니에 저장된 상품이 없습니다.");
        }

        List<OrderDTO> orders = new ArrayList<>();

        Integer totalAmount = 0;


        for(CartEntity cart : carts){
            GoodsEntity goods = cart.getGoods();
            Integer itemPrice = goods.getGoodsPrice();
            Integer itemQuantity = cart.getCartAmount();

            totalAmount += itemPrice * itemQuantity;

            OrderDTO Order = OrderDTO.builder()
                .orderAddress(user.getUserAddress())
                .orderPhone(user.getUserPhone())
                .orderIsCancelled(false)
                .coupon(null)
                .orderRequest(null)
                .orderTotal(totalAmount)
                .orderRecipient(user.getUserName())
                .user(user.getUserPk())
                .goods(goods.getGoodsPk())
                .build();

            orders.add(Order);
            log.info("orders: {}", orders);
        }
        return orders;
    }

    public List<OrderEntity> createCartsOrder(final String userPk,
                                              final List<OrderDTO> temporaryOrder,
                                              final OrderDTO orderDTO,
                                              final Long couponPk) {
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new IllegalArgumentException("로그인을 해주세요."));

        CouponEntity coupon = null;
        CouponUserMappingEntity mapping = null;
        if (couponPk != null) {
            coupon = couponRepository.findById(couponPk)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰이 없습니다."));
            mapping = couponUserMappingRepository.findByUser_UserPkAndCoupon_CouponPk(Long.valueOf(userPk), coupon.getCouponPk())
                .orElseThrow(()-> new IllegalArgumentException("사용 가능한 쿠폰이 없습니다."));
        }

        List<OrderEntity> completedOrders = new ArrayList<>();

        for(OrderDTO order : temporaryOrder){
        OrderEntity newOrder = OrderEntity.builder()
            .orderAddress(orderDTO.getOrderAddress())
            .orderPhone(orderDTO.getOrderPhone())
            .orderRecipient(orderDTO.getOrderRecipient())
            .orderIsCancelled(false)
            .orderTotal(order.getOrderTotal())
            .coupon(mapping!= null ? mapping.getCoupon() : null)
            .orderRequest(orderDTO.getOrderRequest())
            .user(user)
            .build();

            completedOrders.add(orderRepository.save(newOrder));
            log.info("completedOrders: {}", completedOrders);

            GoodsEntity goods = goodsRepository.findById(order.getGoods())
                .orElseThrow(()-> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            OrderListEntity orderList = OrderListEntity.builder()
                .order(newOrder)
                .goods(goods)
                .build();

            orderListRepository.save(orderList);
        }

        return completedOrders;
    }
//
//
//        List<CartEntity> carts = cartRepository.findAllByUser_userPk(Long.valueOf(userPk));
//
//        if(carts.isEmpty()){
//            throw new IllegalArgumentException("장바구니에 저장된 상품이 없습니다.");
//        }
//
//        List<OrderEntity> orders = new ArrayList<>();
//
//        Integer totalAmount = 0;
//
//
//        for(CartEntity cart : carts){
//            GoodsEntity goods = cart.getGoods();
//            Integer itemPrice = goods.getGoodsPrice();
//            Integer itemQuantity = cart.getCartAmount();
//
//            totalAmount += itemPrice * itemQuantity;
//
//            OrderEntity savedOrder = OrderEntity.builder()
//                .orderAddress(orderDTO.getOrderAddress())
//                .orderPhone(orderDTO.getOrderPhone())
//                .orderIsCancelled(false)
//                .coupon(mapping!= null? mapping.getCoupon() : null)
//                .orderRequest(orderDTO.getOrderRequest())
//                .orderTotal(totalAmount)
//                .orderRecipient(orderDTO.getOrderRecipient())
//                .user(user)
//                .build();
//
//            orders.add(orderRepository.save(savedOrder));
//            log.info("orders: {}", orders);
//            OrderListEntity orderList = OrderListEntity.builder()
//                .order(savedOrder)
//                .goods(goods)
//                .build();
//
//            orderListRepository.save(orderList);
//
//        }


    public OrderEntity getOrderList(final String userPk) {
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
           .orElseThrow(() -> new IllegalArgumentException("로그인을 해주세요."));

        return orderRepository.findAllByUser(user);
    }

    public GoodsEntity getOrderGoods(final String userPk, final Long orderPk){
        userRepository.findByUserPk(Long.valueOf(userPk))
           .orElseThrow(() -> new IllegalArgumentException("로그인을 해주세요."));

        OrderListEntity orderList = orderListRepository.findByOrder_orderPk(orderPk);

        GoodsEntity goods = goodsRepository.findByOrderList(orderList);

        return goods;
    }
}

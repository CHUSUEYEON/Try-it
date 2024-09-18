package Try_it.pay;

import Try_it.goods.GoodsEntity;
import Try_it.goods.GoodsRepository;
import Try_it.order.OrderDTO;
import Try_it.order.OrderEntity;
import Try_it.order.OrderRepository;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
public class PayService {
    private final PayRepository payRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;

    @Autowired
    public PayService(PayRepository payRepository, UserRepository userRepository, OrderRepository orderRepository, GoodsRepository goodsRepository) {
        this.payRepository = payRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.goodsRepository = goodsRepository;
    }

    public PayEntity processPaymentDone(PaymentRequestDTO paymentRequestDTO, final String userPk)
        throws IamportResponseException, IOException{
        log.info("paymnet service");
        Long orderPk = paymentRequestDTO.getOrder();
//        Long user = orderDTO.getUser();
        UserEntity checkUser = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(()-> new IllegalArgumentException("로그인을 해주세요."));
//        if(!user.equals(checkUser.getUserPk())){
//            throw new IllegalArgumentException("본인만 결제할 수 있습니다.");
//        }


        OrderEntity order = orderRepository.findById(orderPk)
            .orElseThrow(()-> new NoSuchElementException("주문 정보를 찾을 수 없습니다."));

        Long goodsPk = paymentRequestDTO.getGoods();
        GoodsEntity goods = goodsRepository.findById(goodsPk)
            .orElseThrow(()-> new NoSuchElementException("상품을 찾을 수 없습니다."));

        String paycode = UUID.randomUUID().toString();
        PayEntity pay = PayEntity.builder()
            .payCode(paycode)
            .payIsRefunded(false)
            .order(order)
            .goods(goods)
            .build();

        payRepository.save(pay);

//        List<Long> goodsList = orderDTO.getGoods();
//
//        OrderEntity order = orderRepository.findById(orderPk)
//            .orElseThrow(()-> new NoSuchElementException("주문 정보를 찾을 수 없습니다."));
//
//        for(Long good : goodsList){
//            GoodsEntity goods = goodsRepository.findById(good)
//                .orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다."));
//            String paycode = UUID.randomUUID().toString();
//
//            PayEntity pay = PayEntity.builder()
//                .payCode(paycode)
//                .payIsRefunded(false)
//                .goods(goods)
//                .order(order)
//                .build();
//
//            payRepository.save(pay);
//        }
//        checkDuplicatePayment(PaymentDTO);
        return null;
    }
}

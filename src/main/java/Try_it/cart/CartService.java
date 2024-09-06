package Try_it.cart;

import Try_it.goods.GoodsEntity;
import Try_it.goods.GoodsRepository;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;

    @Autowired
    public CartService(CartRepository cartRepository, UserRepository userRepository, GoodsRepository goodsRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.goodsRepository = goodsRepository;
    }

    public CartEntity createCart(final String userPk,
                                 final Long goodsPk){

        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(()-> new RuntimeException("로그인을 해주세요."));

        GoodsEntity goods = goodsRepository.findById(goodsPk)
            .orElseThrow(()-> new RuntimeException("해당되는 상품이 없습니다."));

        List<CartEntity> cart = cartRepository.findAllByUser_userPk(Long.valueOf(userPk));

        if(goods.getGoodsDeletedAt()!= null){
            throw new RuntimeException("삭제된 상품은 장바구니에 담을 수 없습니다.");
        }

        CartEntity newCart = CartEntity.builder()
            .cartAmount(cart == null ? 1 : cart.size() + 1 )
            .cartIsPaid(false)
            .goods(goods)
            .user(user)
            .build();

        return cartRepository.save(newCart);
    }

    public void deleteGoodsInCart(final String userPk,
                                  final Long goodsPk){
        userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(()-> new RuntimeException("로그인을 해주세요."));

        goodsRepository.findById(goodsPk)
            .orElseThrow(()->new RuntimeException("해당되는 상품이 없습니다."));

        List<CartEntity> carts = cartRepository.findAllByUser_userPk(Long.valueOf(userPk));
        log.info("carts : {}", carts);

        if(carts == null){
            throw new RuntimeException("장바구니에 저장된 상품이 없습니다.");
        }

        for(CartEntity cart : carts){
            log.info("** cart : {}", cart.getGoods().getGoodsPk());

        if(cart.getGoods().getGoodsPk() == goodsPk){
            cartRepository.delete(cart);
        }
        }


        }
        public void deleteCart(final String userPk){
            UserEntity user = userRepository.findById(Long.valueOf(userPk))
                .orElseThrow(()-> new RuntimeException("로그인을 해주세요"));

            List<CartEntity> carts = cartRepository.findAllByUser_userPk(Long.valueOf(userPk));

            if(carts == null){
                throw new RuntimeException("장바구니에 저장된 상품이 없습니다.");
            } else {
                cartRepository.deleteAll(carts);
            }
    }

}

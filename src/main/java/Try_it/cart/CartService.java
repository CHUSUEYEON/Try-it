package Try_it.cart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartEntity createCart(final String userPk,
                                 final CartDTO cartDTO,
                                 final Long goodsPk){
        return null;
    }
        // 유저 pk, 상��� pk, ���기상태 false(미결제))
}

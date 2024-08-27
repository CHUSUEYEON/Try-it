package Try_it.favorites;

import Try_it.goods.GoodsEntity;
import Try_it.goods.GoodsRepository;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FavoritesService {
    private final FavoritesRepository favoritesRepository;
    private final UserRepository userRepository;
    final private GoodsRepository goodsRepository;

    public FavoritesService(FavoritesRepository favoritesRepository, UserRepository userRepository, GoodsRepository goodsRepository) {
        this.favoritesRepository = favoritesRepository;
        this.userRepository = userRepository;
        this.goodsRepository = goodsRepository;
    }

    public FavoritesEntity create(final String userPk,
                                  final Long goodsPk) {
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));

        GoodsEntity goods = goodsRepository.findById(goodsPk).orElseThrow(()
            -> new RuntimeException("해당되는 상품이 없습니다."));

        if (favoritesRepository.findByUserAndGoods(user.getUserPk(), goods.getGoodsPk()) != null){
            throw new RuntimeException("이미 찜 추가한 상품입니다.");
        }

        FavoritesEntity newFavorites = FavoritesEntity.builder()
            .user(user)
            .goods(goods)
            .build();

        return favoritesRepository.save(newFavorites);
    }
}

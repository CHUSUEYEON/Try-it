package Try_it.favorites;

import Try_it.goods.GoodsEntity;
import Try_it.goods.GoodsRepository;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if(goods.getGoodsDeletedAt() != null){
            throw new RuntimeException("삭제된 상품에는 찜 추가를 할 수 없습니다.");
        }

        if (favoritesRepository.findByUserAndGoods(user.getUserPk(), goods.getGoodsPk()) != null){
            throw new RuntimeException("이미 찜 추가한 상품입니다.");
        }

        FavoritesEntity newFavorites = FavoritesEntity.builder()
            .user(user)
            .goods(goods)
            .build();

        return favoritesRepository.save(newFavorites);
    }

    public FavoritesEntity delete(final String userPk,
                                  final Long favPk){
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));

        FavoritesEntity favorite = favoritesRepository.findById(favPk).orElseThrow(()
        -> new RuntimeException("삭제할 찜이 없습니다."));

        if(user!= favorite.getUser()) throw new IllegalStateException("자신이 찜한 상품만 삭제할 수 있습니다.");

        favoritesRepository.delete(favorite);
        return favorite;
    }

    public List<FavoritesEntity> get(final String userPk){
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));

        List<FavoritesEntity> favorites = favoritesRepository.findAllByUserPk(user.getUserPk());

        if(favorites == null) throw new IllegalStateException("불러올 찜 목록이 없습니다.");

        return favorites;
    }
}

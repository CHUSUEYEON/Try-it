package Try_it.favorites;

import Try_it.goods.GoodsEntity;
import Try_it.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<FavoritesEntity, Long> {

    @Query("SELECT f FROM FavoritesEntity f WHERE f.user.userPk = :userPk AND f.goods.goodsPk = :goodsPk")
    FavoritesEntity findByUserAndGoods(@Param("userPk") Long userPk,
                                       @Param("goodsPk") Long goodsPk);

    @Query("SELECT f FROM FavoritesEntity f WHERE f.user.userPk = :userPk AND f.goods.goodsDeletedAt IS NULL")
    List<FavoritesEntity> findAllByUserPk(@Param("userPk") Long userPk);
}

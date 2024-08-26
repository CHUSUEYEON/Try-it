package Try_it.category;

import Try_it.goods.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsCategoriesMappingRepository extends JpaRepository<GoodsCategoriesMappingEntity, Long> {

    List<GoodsCategoriesMappingEntity> findByGoods(GoodsEntity goods);
}

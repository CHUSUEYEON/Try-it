package Try_it.goods;

import Try_it.goods.entity.GoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminGoodsRepository extends JpaRepository<GoodsEntity, Long>{

    @Query("SELECT g FROM GoodsEntity g WHERE g.goodsName Like %:keyword%")
    List<GoodsEntity> findGoodsByKeyword(@Param("keyword") String keyword);

//    Page<GoodsDTO> findAllGoodsList(Pageable pageable, String sort, String direction, GoodsFilterDTO filter);
}
